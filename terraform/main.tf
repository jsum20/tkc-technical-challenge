# main.tf

provider "aws" {
  region = "eu-west-2"
}

terraform {
  backend "s3" {
    bucket = "hstc-api-jar"
    key = "terraform.tfstate"
    region = "eu-west-2"
    dynamodb_table = "terraform-lock"
  }
}

# IAM Role for EC2 instance
resource "aws_iam_role" "hstc_api_role" {
  name = "hstc-api-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      },
    ]
  })
}

# IAM Policy to allow the EC2 instance to access S3 bucket
resource "aws_iam_policy" "s3_access_policy" {
  name = "s3-access-policy"
  description = "Allow EC2 instance to access S3 bucket"
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "s3:GetObject"
        Effect = "Allow"
        Resource = "arn:aws:s3:::hstc-api-jar/*"
      }
    ]
  })
}

# Attach the policy to the role
resource "aws_iam_policy_attachment" "hstc_s3_policy_attachment" {
  name = "hstc-s3-policy-attachment"
  policy_arn = aws_iam_policy.s3_access_policy.arn
  roles = [aws_iam_role.hstc_api_role.name]
}

# Attach IAM role to EC2 instance
resource "aws_iam_instance_profile" "hstc_api_profile" {
  name = "hstc-api-profile"
  role = aws_iam_role.hstc_api_role.name
}

# Security Group for EC2 Instance
resource "aws_security_group" "hstc_sg" {
  name = "hstc-sg"
  description = "Allow HTTP and SSH traffic"

  # Https access for the springboot API
  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # SSH access
  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # outbound
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Security Group for RDS Instance
resource "aws_security_group" "rds_sg" {
  name = "rds-sg"
  description = "Allow PostgreSQL traffic from EC2 instance"

  ingress {
    from_port = 5432
    to_port = 5432
    protocol = "tcp"
    security_groups = [aws_security_group.hstc_sg.id]
  }

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# RDS PostgreSQL Instance
resource "aws_db_instance" "postgres" {
  identifier = "hstc-postgres-db"
  engine = "postgres"
  engine_version = "14"
  instance_class = "db.t3.micro"
  allocated_storage = 20
  storage_type = "gp2"
  username = "dbadmin"
  password = "Passw0rd123!"
  parameter_group_name = "default.postgres14"
  skip_final_snapshot = true
  publicly_accessible = false
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
}

# Creating the EC2 Instance
resource "aws_instance" "hstc_api" {
  ami = var.ami_id
  instance_type = var.instance_type
  security_groups = [aws_security_group.hstc_sg.name]
  iam_instance_profile = aws_iam_instance_profile.hstc_api_profile.name

  tags = {
    Name = "HSTC-API-Server"
  }

  # Script to install Java
  user_data = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo yum install -y  postgresql

              sudo yum install -y java-17-amazon-corretto-devel

              # Download the JAR file from S3 (or GitHub Releases)
              aws s3 cp s3://hstc-api-jar/tkc-api-1.0.0.jar /opt/tkc-api-1.0.0.jar

              chmod +x /opt/tkc-api-1.0.0.jar

              # Download data.sql
              aws s3 cp s3://hstc-api-jar/data.sql /opt/data.sql

              echo "export DB_URL=jdbc:postgresql://${aws_db_instance.postgres.endpoint}/hstc_db" >> /home/ec2-user/.bashrc
              echo "export DB_USERNAME=dbadmin" >> /home/ec2-user/.bashrc
              echo "export DB_PASSWORD=Passw0rd123!" >> /home/ec2-user/.bashrc
              source /home/ec2-user/.bashrc

              # Create the database and initialize it
              PGPASSWORD=Passw0rd123! psql -h ${aws_db_instance.postgres.endpoint} -U dbadmin -d postgres -c "CREATE DATABASE hstc_db;"
              PGPASSWORD=Passw0rd123! psql -h ${aws_db_instance.postgres.endpoint} -U dbadmin -d hstc_db -f /opt/data.sql

              # Run the app
              java -jar /opt/tkc-api-1.0.0.jar &
              EOF
}

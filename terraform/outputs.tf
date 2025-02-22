# outputs.tf

output "public_ip" {
  description = "The public IP of the EC2 instance"
  value = aws_instance.hstc_api.public_ip
}

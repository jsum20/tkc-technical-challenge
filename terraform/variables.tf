# variables.tf

variable "ami_id" {
  description = "The AMI ID to use for the EC2 instance."
  type = string
  default = "ami-0076be86944570bff"
}

variable "instance_type" {
  description = "The instance type for the EC2 instance."
  type = string
  default = "t2.micro"
}




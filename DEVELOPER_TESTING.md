# Developer Testing Guide

## Image Uploads
Currently, image uploads for garment photos are stored locally in the `uploads/` directory at the root of the project.
This directory is created automatically on the first upload. 
Static files are served directly from this folder via the `/uploads/**` endpoint.

**TODO for Future Production Integration:**
This local storage mechanism must be replaced with AWS S3, Firebase Storage, or another cloud object storage provider in production environments. 
- You will need to implement an `S3FileStorageService` implementing the same interface/methods as `FileStorageService`.
- Replace the `multipartFile.transferTo(localFile)` logic with the AWS SDK `s3Client.putObject(...)`.

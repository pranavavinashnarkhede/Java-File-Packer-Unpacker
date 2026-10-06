# Java File Packer-Unpacker

A Java-based file packing and unpacking application that combines multiple supported files into a single `.pak` file and restores them when required.

## Features

* Pack multiple files into a single `.pak` file
* Unpack files from a `.pak` file
* Supports `.txt`, `.c`, `.cpp`, `.java`, `.py`, and `.pdf` files
* Stores file name and file size in the packed file header
* Uses 1024-byte buffer-based file processing
* Applies XOR-based data transformation
* Skips unsupported files and directories
* Prevents the generated `.pak` file from being packed again
* Provides Overwrite, Skip, and Cancel options during unpacking
* Performs basic input and file validation
* Uses try-with-resources for automatic resource management
* Provides packing statistics
* Command-line interface (CUI)

## Technologies

* Java
* File Handling
* Byte Streams
* Java Collections
* Exception Handling
* Try-with-Resources
* Buffer-Based Processing
* XOR Operation

## Project Structure

```text
Java-File-Packer-Unpacker/
│
├── README.md
├── DOCUMENTATION.md
├── .gitignore
│
└── src/
    ├── FilePacker.java
    └── FileUnpacker.java
```

## How It Works

### Packing

The packer reads supported files from a source folder and combines them into a single `.pak` file.

```text
Source Folder
      ↓
Supported Files
      ↓
File Header + File Data
      ↓
XOR Transformation
      ↓
Packed .pak File
```

### Unpacking

The unpacker reads the `.pak` file and restores the original files.

```text
.pak File
      ↓
Read File Header
      ↓
Read Stored Data
      ↓
Reverse XOR Transformation
      ↓
Restored Files
```

## Supported File Types

```text
.txt
.c
.cpp
.java
.py
.pdf
```

## How to Run

Navigate to the `src` directory.

### Compile

```bash
javac FilePacker.java
javac FileUnpacker.java
```

### Run File Packer

```bash
java FilePacker
```

Example:

```text
Enter folder name: TestFiles
Enter packed file name: PackedFile.pak
```

### Run File Unpacker

```bash
java FileUnpacker
```

Example:

```text
Enter packed file name: PackedFile.pak
```

If an extracted file already exists, the application provides options to:

```text
1. Overwrite
2. Skip
3. Cancel
```

## Important Note

The project uses XOR-based data transformation for learning purposes. It is **not secure encryption** and should not be used for protecting sensitive data.

## Documentation

For detailed technical information about the project, including the internal packing and unpacking process, file format, buffer-based processing, resource management, validation, architecture, limitations, and future improvements, see:

**[DOCUMENTATION.md](DOCUMENTATION.md)**

## Purpose

This project was developed to practice and demonstrate Java file handling, byte streams, buffer-based processing, collections, exception handling, resource management, and file packing/unpacking concepts.

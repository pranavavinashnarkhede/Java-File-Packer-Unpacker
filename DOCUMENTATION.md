# Java File Packer-Unpacker Documentation

## 1. Project Overview

Java File Packer-Unpacker is a CUI-based Java project that packs multiple supported files into a single packed file and extracts them back when required.

The project demonstrates Java file handling, byte streams, collections, exception handling, XOR transformation, and resource management.

---

## 2. Project Structure

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

---

## 3. Supported File Types

The project currently supports:

```text
.txt
.c
.cpp
.java
.py
.pdf
```

Unsupported file types are skipped during packing.

---

## 4. File Packing

`FilePacker.java` reads supported files from the specified folder and stores them in a single packed file.

### Process

1. Validate the source folder and packed file path.
2. Read files from the source folder.
3. Skip directories and unsupported file types.
4. Create a 100-byte header for each file.
5. Store the file name and original file size in the header.
6. Read the file using a 1024-byte buffer.
7. Apply XOR transformation to the data.
8. Write the header and transformed data to the packed file.

### Header Format

```text
FileName#FileSize
```

Example:

```text
sample.txt#150
```

The remaining header space is filled with blank characters so that each header occupies exactly 100 bytes.

---

## 5. File Unpacking

`FileUnpacker.java` reads the packed file and recreates the original files.

### Process

1. Validate the packed file.
2. Read the 100-byte header.
3. Extract the file name and file size.
4. Create the output file.
5. Read the stored file data.
6. Apply the same XOR transformation to restore the original data.
7. Write the restored data to the output file.
8. Continue until all files are extracted.

---

## 6. XOR Transformation

The project uses XOR as a basic data transformation technique.

```java
byte XOR_KEY = 65;
```

During packing:

```text
Original Data
      ↓
XOR with Key
      ↓
Packed Data
```

During unpacking:

```text
Packed Data
      ↓
XOR with Same Key
      ↓
Original Data
```

The same XOR key is used in both operations.

> **Note:** XOR transformation in this project is for learning purposes and is not considered secure encryption.

---

## 7. Buffer-Based Processing

The project uses a 1024-byte buffer to process files in chunks.

```java
byte Buffer[] = new byte[1024];
```

This allows the program to process file data in smaller portions instead of loading the entire file into memory at once.

---

## 8. Resource Management

The project uses **try-with-resources** for file streams.

Example:

```java
try(FileInputStream fiobj =
        new FileInputStream(file))
{
    // File processing
}
```

Streams are automatically closed after processing, helping prevent resource leaks.

---

## 9. Input Validation

The project performs basic validation for:

* Empty input values
* Non-existing folders
* Invalid folder paths
* Non-existing packed files
* Invalid file paths
* Invalid packed-file headers
* Unsupported file extensions

---

## 10. Execution

Compile the files from the `src` directory:

```bash
javac FilePacker.java
javac FileUnpacker.java
```

Run the packer:

```bash
java FilePacker
```

Run the unpacker:

```bash
java FileUnpacker
```

---

## 11. Java Concepts Used

* Classes and Objects
* Methods
* File Handling
* `FileInputStream`
* `FileOutputStream`
* Byte Arrays
* Buffer-Based Processing
* `HashSet`
* String Operations
* Exception Handling
* Try-with-Resources
* `Scanner`
* XOR Operation

---

## 12. Limitations

* Only selected file extensions are supported.
* The header size is fixed at 100 bytes.
* File names that exceed the available header space cannot be stored.
* XOR transformation is not suitable for secure encryption.
* The application currently uses a command-line interface.

---

## 13. Future Improvements

* Support additional file types
* Improve the packed-file format
* Add file integrity verification
* Add compression
* Implement secure encryption
* Improve large-file handling
* Add progress information

import java.io.*;
import java.util.*;

class FileUnpacker
{
    private static final byte XOR_KEY = 65;

    private int ExtractedFileCount = 0;
    private int SkippedFileCount = 0;
    private long TotalSize = 0;
    private boolean isCancelled = false ;

    /*
    Function Name : unpack
    Description   : Extracts files from the packed file.
    Input         : Packed file name
    Output        : Extracted files
    */

    public void unpack(String PackedFileName , Scanner sobj) throws Exception
    {
        File fpackobj = null;

        String strHeader = null;
        File NewFile = null;

        byte Header[] = new byte[100];
        String Tokens[] = null;

        byte Buffer[] = null;
        byte transformedBuffer[] = null;

        int iRet = 0;
        int i = 0;

        // Validate packed file name
        if(PackedFileName == null || PackedFileName.trim().isEmpty())
        {
            throw new Exception("Packed file name cannot be empty.");
        }

        // Validate packed file
        fpackobj = new File(PackedFileName);

        // if PackedFile length is less than 100 means it is invalid PackedFile
        if(fpackobj.length() < 100)
        {
            throw new Exception("Invalid Packed File");
        }

        // Extract the file name of packed file name
        String packedFileName = fpackobj.getName();

        // get the starting index of extension
        int dotIndex = packedFileName.lastIndexOf('.');

        String extension = "";

        if(dotIndex != -1)
        {
            // get the extension of packed file
            extension = packedFileName.substring(dotIndex+1).toLowerCase();
        }

        if(!extension.equals("pak"))
        {
            throw new Exception("Invalid packed file . Expected a .pak file");
        }

        if(!fpackobj.exists())
        {
            throw new Exception("Packed file does not exist: " +PackedFileName);
        }

        if(!fpackobj.isFile())
        {
            throw new Exception("The specified path is not a packed file.");
        }

        // Open packed file and close it automatically
        try(FileInputStream fiobj = new FileInputStream(fpackobj))
        {
            /*
            Read one 100-byte header at a time.
            The header contains the file name and file size.
            */

            while(true)
            {
                int headerBytesRead = 0;

                // Read exactly 100 bytes for the header
                while(headerBytesRead < 100)
                {
                    iRet = fiobj.read(Header, headerBytesRead, 100 - headerBytesRead);

                    if(iRet == -1)
                    {
                        break;
                    }

                    headerBytesRead = headerBytesRead + iRet;
                }

                // End of packed file
                if(headerBytesRead == 0)
                {
                    break;
                }

                // Header must contain exactly 100 bytes
                if(headerBytesRead < 100)
                {
                    throw new Exception("Invalid packed file: incomplete header.");
                }


                // Convert header bytes into String
                strHeader = new String(Header);

                strHeader = strHeader.trim();

                strHeader = strHeader.replaceAll("\\s+", " ");


                // Extract file name and file size
                Tokens = strHeader.split("#");

                // Validate header format
                if(Tokens.length != 2)
                {
                    throw new Exception("Invalid packed file header.");
                }

                String fileName = Tokens[0].trim();

                // Validate extracted file name
                if(fileName.isEmpty())
                {
                    throw new Exception( "Invalid file name in packed file.");
                }

                File extractedFile = new File(fileName);

                if(!extractedFile.getName().equals(fileName))
                {
                    throw new Exception("Invalid file path in packed file : "+fileName);
                }

                int fileSize;

                // Convert file size from String to integer
                try
                {
                    fileSize = Integer.parseInt(Tokens[1].trim());
                }
                catch(NumberFormatException e)
                {
                    throw new Exception("Invalid file size in packed file.");
                }

                // File size cannot be negative
                if(fileSize < 0)
                {
                    throw new Exception("Invalid file size in packed file.");
                }


                // Create the extracted file
                NewFile = extractedFile ;

                boolean skip = false;

                // Prevent accidental overwrite
                if(NewFile.exists())
                {
                    System.out.println("File already exists : "+NewFile.getName());
                    System.out.println();

                    while(true)
                    {
                        int choice = 0 ;
                        
                        System.out.println("What do you want to do (1 -> Overwrite . 2-> Skip . 3-> Cancel) : ");
                        
                        if(sobj.hasNextInt())
                        {
                            choice = sobj.nextInt();
                        }
                        else
                        {
                            System.out.println("Please enter a number.");
                            sobj.next();   // remove the invalid input
                        }
                        
                        if(choice < 1 || choice > 3)
                        {
                            System.out.println("Invalid Choice");
                            System.out.println("Please Enter Valid Choice : ");
                            continue;
                        }

                        if(choice == 1)
                        {
                            break;
                        }
                        else if(choice == 2)
                        {
                            SkippedFileCount++;
                            skip = true;
                            break;
                        }
                        else if(choice == 3)
                        {
                            isCancelled = true;
                            break;
                        }    
                        else
                        {
                            System.out.println("Invalid choice");
                        }
                    }

                    if(skip)
                    {
                        continue;
                    }

                    if(isCancelled)
                    {
                        break;
                    }

                }

                // Open output file and close it automatically
                try(FileOutputStream foobj = new FileOutputStream(NewFile))
                {
                    Buffer = new byte[1024];

                    transformedBuffer = new byte[1024];

                    /*
                    Read the complete file data
                    according to the size stored in the header.
                    */

                    int totalBytesRead = 0;

                    while(totalBytesRead < fileSize)
                    {
                        iRet = fiobj.read(Buffer, 0, Math.min(Buffer.length , fileSize - totalBytesRead));

                        if(iRet == -1)
                        {
                            throw new Exception("Unexpected end of packed file while reading: "+ NewFile.getName());
                        }

                        totalBytesRead = totalBytesRead + iRet;

                        /*
                            Read the file data in chunks
                            according to the size stored in the header.
                        */

                        for(i = 0; i < iRet; i++)
                        {
                            transformedBuffer[i] = (byte)(Buffer[i] ^ XOR_KEY);
                        }


                        // Write the restored data to the extracted file
                        foobj.write(transformedBuffer, 0, iRet);

                    }                    

                    ExtractedFileCount++;
                    TotalSize = TotalSize + fileSize;

                } // Output stream automatically closed here

            }
        } // Input stream automatically closed here
    }

    public int getExtractedFileCount()
    {
        return ExtractedFileCount;
    }

    public long getTotalSize()
    {
        return TotalSize;
    }

    public int getSkippedFileCount()
    {
        return SkippedFileCount;
    }

    public boolean isCancelled()
    {
        return isCancelled;
    }

    /*
    Function Name : main
    Description   : Takes packed file name from the user
                    and starts the unpacking process.
    */

    public static void main(String A[])
    {

        Scanner sobj = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("          JAVA FILE UNPACKER");
        System.out.println("========================================");

        System.out.print("Enter packed file name: ");

        String packedFileName = sobj.nextLine().trim();

        try
        {
            FileUnpacker upobj =new FileUnpacker();

            upobj.unpack(packedFileName , sobj);

            if(upobj.isCancelled())
            {
                System.out.println();
                System.out.println("========================================");
                System.out.println("          UNPACKING CANCELLED");
                System.out.println("========================================");

            }
            else
            {
                System.out.println();

                System.out.println("========================================");
                System.out.println("          UNPACKING COMPLETED");
                System.out.println("========================================");

                System.out.println();
                System.out.println("  Files Extracted : "+ upobj.getExtractedFileCount());
                System.out.println("  Files Skipped   : "+ upobj.getSkippedFileCount());
                System.out.println("  Total Size      : "+ String.format("%.2f",upobj.getTotalSize() / 1024.0)+ " KB");

                System.out.println();
                System.out.println("----------------------------------------");
                System.out.println("  Packed File     : " + packedFileName);
                System.out.println("----------------------------------------");

                System.out.println("Thank you for using Java File Unpacker!");

            }

        }
        catch(Exception e)
        {
            System.out.println();

            System.out.println("Unpacking failed: "+ e.getMessage());
        }
    }
}


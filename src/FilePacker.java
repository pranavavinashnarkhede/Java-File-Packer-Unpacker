import java.io.*;
import java.util.*;

class FilePacker
{
    private int PackedFileCount = 0;
    private int SkippedFileCount = 0;
    private long TotalSize = 0;

    /*
    Function Name : pack
    Description   : Packs supported files from a folder into
                    a single packed file.
    Input         : Folder name, packed file name
    Output        : Packed file
    */

    public void pack(String FolderName, String PackedFileName) throws Exception
    {
        String header = "";

        byte Buffer[] = new byte[1024];
        byte bHeader[] = null;
        byte transformedBuffer[] = new byte[1024];

        int i = 0, j = 0;
        int iRet = 0;
        int Size = 0;

        byte XOR_KEY = 65;

        String str = null;
        String extension = "";
        int dotIndex = 0;

        // Validate folder name
        if(FolderName == null || FolderName.trim().isEmpty())
        {
            throw new Exception("Folder name cannot be empty.");
        }

        // Validate packed file name
        if(PackedFileName == null || PackedFileName.trim().isEmpty())
        {
            throw new Exception("Packed file name cannot be empty.");
        }

        // Validate source folder
        File fobjfolder = new File(FolderName);

        if(!fobjfolder.exists())
        {
            throw new Exception("Folder does not exist: " + FolderName);
        }

        if(!fobjfolder.isDirectory())
        {
            throw new Exception("The specified path is not a folder.");
        }


        // Validate packed file path
        File fobjpack = new File(PackedFileName);

        if(fobjpack.exists() && fobjpack.isDirectory())
        {
            throw new Exception("The specified packed file path " + PackedFileName + " is a directory.");
        }


        // Get files present in the source folder
        File fArr[] = fobjfolder.listFiles();

        if(fArr == null)
        {
            throw new Exception("Unable to read the folder.");
        }

        /*
        Define the file extensions supported
        by the packing process.
        */

        Set<String> supportedExtensions = new HashSet<>();

        supportedExtensions.add("txt");
        supportedExtensions.add("c");
        supportedExtensions.add("cpp");
        supportedExtensions.add("java");
        supportedExtensions.add("py");
        supportedExtensions.add("pdf");

        // Open packed file and close it automatically
        try(FileOutputStream foobj = new FileOutputStream(fobjpack))
        {
            for(i = 0; i < fArr.length; i++)
            {
                // Skip directories
                if(!fArr[i].isFile())
                {
                    SkippedFileCount++;
                    continue;
                }

                str = fArr[i].getName();

                /*
                Extract the file extension
                using the last dot in the file name.
                */

                dotIndex = str.lastIndexOf('.');

                extension = "";

                if(dotIndex != -1)
                {
                    extension = str.substring(dotIndex + 1).toLowerCase();
                }

                // Process only supported file types
                if(supportedExtensions.contains(extension))
                {
                    /*
                    Create a fixed-size header containing
                    the file name and original file size.
                    */

                    header = fArr[i].getName();

                    header = header + "#";

                    header = header + fArr[i].length();

                    /*
                    The header must fit within
                    the fixed 100-byte header area.
                    */

                    if(header.length() > 100)
                    {
                        throw new Exception("File name is too long to store in packed file: "+ fArr[i].getName());
                    }

                    /*
                    Add spaces so that every header
                    occupies exactly 100 bytes.
                    */

                    Size = 100 - header.length();

                    for(j = 0; j < Size; j++)
                    {
                        header = header + " ";
                    }

                    bHeader = header.getBytes();

                    // Write the fixed-size header
                    foobj.write(bHeader);

                    // Open source file and close it automatically
                    try(FileInputStream fiobj = new FileInputStream(fArr[i]))
                    {
                        /*
                        Read the source file in
                        1024-byte chunks.
                        */

                        while((iRet = fiobj.read(Buffer)) != -1)
                        {
                            /*
                            Apply XOR transformation
                            before storing the file data.
                            */

                            for(int k = 0; k < iRet; k++)
                            {
                                transformedBuffer[k] =(byte)(Buffer[k] ^ XOR_KEY);
                            }

                            // Write transformed data to packed file
                            foobj.write(transformedBuffer, 0,iRet);
                        }

                        PackedFileCount++;
                        TotalSize = TotalSize + fArr[i].length();

                    }

                    // fiobj automatically closes here

                    header = "";
                }
                else
                {
                    SkippedFileCount++;
                }

            }
        }

        // foobj automatically closes here
    }

    public int getPackedFileCount()
    {
        return PackedFileCount;
    }

    public int getSkippedFileCount()
    {
        return SkippedFileCount;
    }

    public long getTotalSize()
    {
        return TotalSize;
    }

    public static void main(String A[]) throws Exception
    {
        Scanner sobj = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("           JAVA FILE PACKER");
        System.out.println("========================================");
    
        System.out.print("Enter folder name: ");
        String FolderName = sobj.nextLine().trim();

        System.out.print("Enter packed file name: ");
        String PackedFileName = sobj.nextLine().trim();

        try
        {
            FilePacker pobj = new FilePacker();

            pobj.pack(FolderName, PackedFileName);

            System.out.println();

            System.out.println("========================================");
            System.out.println("           PACKING COMPLETED");
            System.out.println("========================================");

            System.out.println();
            System.out.println("  Packed Files   : " + pobj.getPackedFileCount());
            System.out.println("  Skipped Files  : " + pobj.getSkippedFileCount());
            System.out.println("  Total Size     : " + pobj.getTotalSize() + " bytes");

            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("  Packed File    : " + PackedFileName);
            System.out.println("----------------------------------------");

            System.out.println();
            System.out.println("Thank you for using Java File Packer!");    

        }
        catch(Exception e)
        {
            System.out.println();
            System.out.println("Packing failed: " + e.getMessage());
        }

        sobj.close();
    }
}


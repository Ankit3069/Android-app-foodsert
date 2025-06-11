package com.example.personalizedecommerceapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

public class FileUtils {

    public static Uri getFileUri(Context context, File file) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String authority = context.getApplicationContext().getPackageName() + ".provider";
                return FileProvider.getUriForFile(context, authority, file);
            } else {
                return Uri.fromFile(file);
            }
        } catch (Exception exception) {
            return null;
        }
    }

    public static String getFileName(String filePath) {
        String fileName = "Unknown";
        try {
            File file = new File(filePath);
            fileName = file.getName();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return fileName;
    }

    private static String getFileExtension(String filePath) {
        String extension = "";
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i + 1);
        }
        return extension.trim().equals("") ? "pdf" : extension;
    }

    public static File checkForExistingFileAndGetModifiedFileName(Context context, File file) {
        if (file.exists()) {
            String newFileName = file.getName();
            String simpleName = newFileName;
            if (file.getName().contains(".")) {
                simpleName = file.getName().substring(0, newFileName.indexOf("."));
            }

            String strDigit = "";

            try {
                simpleName = (Integer.parseInt(simpleName) + 1 + "");
                File newFile = new File(file.getParent() + "/"
                        + simpleName + "." + getFileExtension(file.getAbsolutePath()));
                return checkForExistingFileAndGetModifiedFileName(context, newFile);
            } catch (Exception ignored) {
            }

            for (int i = simpleName.length() - 1; i >= 0; i--) {
                if (!Character.isDigit(simpleName.charAt(i))) {
                    strDigit = simpleName.substring(i + 1);
                    simpleName = simpleName.substring(0, i + 1);
                    break;
                }
            }

            if (strDigit.length() > 0) {
                simpleName = simpleName + (Integer.parseInt(strDigit) + 1);
            } else {
                simpleName += "1";
            }

            File newFile = new File(file.getParent() + "/" + simpleName
                   +"." + getFileExtension(file.getAbsolutePath()));
            return checkForExistingFileAndGetModifiedFileName(context, newFile);
        }
        return file;
    }

    public static void viewFile(Context context, String filePath) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Uri uri = getFileUri(context, file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.setDataAndType(uri, "application/pdf");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
                    List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        context.grantUriPermission(packageName, uri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                context.startActivity(Intent.createChooser(intent, "View"));
            } else {
                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(context, "No application found to open the file", Toast.LENGTH_SHORT).show();
        }
    }

    public static File copyFile(Context context, Uri sourceFileUri) {
        boolean result = false;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File destinationFile = null;
        try {
            String filePath = PathUtils.getPath(context, sourceFileUri);
            String extension = null;
            if (filePath != null) extension = getFileExtension(filePath);
            extension = extension == null || extension.trim().equals("") ? "pdf" : extension;
            CharSequence format = android.text.format.DateFormat.format("yyyyMMddhhmmss", new Date());
            String fileName = "image_" + format + "." + extension;
            File folder = new File(Constants.FILE_SAVE_DESTINATION);
            if (!folder.exists()) folder.mkdirs();
            destinationFile = new File(folder.getAbsolutePath() + File.separator + fileName);
            if (!destinationFile.exists()) destinationFile.createNewFile();
            inputStream = context.getContentResolver().openInputStream(sourceFileUri);
            outputStream = new FileOutputStream(destinationFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result ? destinationFile : null;
    }

    public static File downloadFile(Context context, String sourceFilePath, String destinationFileName) {
        boolean result = false;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File destinationFile = null;
        try {
            Uri sourceFileUri = getFileUri(context, new File(sourceFilePath));
            String fileExtension = getFileExtension(sourceFilePath);
            File folder = new File(getDownloadDirectory());
            if (!folder.exists()) folder.mkdirs();
            destinationFile = new File(folder.getAbsolutePath() + File.separator
                    + destinationFileName + "." + fileExtension);
            if (!destinationFile.exists()) destinationFile.createNewFile();
            else {
                Uri fileUri = getFileUri(context, destinationFile);
                destinationFile = checkForExistingFileAndGetModifiedFileName(context,
                        new File(PathUtils.getPath(context, fileUri)));
                destinationFile.createNewFile();
            }
            inputStream = context.getContentResolver().openInputStream(sourceFileUri);
            outputStream = new FileOutputStream(destinationFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result ? destinationFile : null;
    }

    public static String getDownloadDirectory() {
        try {
            File directory = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            return directory.getAbsolutePath() + File.separator;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

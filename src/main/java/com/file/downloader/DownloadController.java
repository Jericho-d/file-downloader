package com.file.downloader;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.Currency;

import static org.apache.tika.mime.MediaType.TEXT_HTML;

public class DownloadController {

    private static final String FILE_PATH = "F:\\New Folder\\%s\\%s%s";

    private final Tika tika = new Tika();

    protected void onButtonClick(
            final String stringUrl,
            final int id
    ) {
        final var url = getEncodedUrl(stringUrl, id);
        final var mimeType = decodeMimeType(url);
        final var typeName = mimeType.getName();
        final var folderName = typeName.substring(0, typeName.indexOf("/"));
        final var extension = mimeType.getExtension();

        if (!typeName.startsWith(TEXT_HTML.getType())) {
            downloadFileById(url, FILE_PATH.formatted(folderName, id, extension));

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SneakyThrows
    private static URL getEncodedUrl(String stringUrl, int id) {
        final var base64Id = Base64.getEncoder()
                .encodeToString(String.valueOf(id).getBytes());

        return new URI(stringUrl + base64Id).toURL();
    }

    private void downloadFileById(
            final URL url,
            final String filePath
    ) {
        try {
            FileUtils.copyURLToFile(url, new File(filePath));
        } catch (IOException e) {
            System.out.println("File wasn't found");
        }
    }

    private MimeType decodeMimeType(
            final URL url
    ) {
        MimeType mimeType;
        try {
            final var type = tika.detect(url);
            mimeType = MimeTypes.getDefaultMimeTypes().forName(type);
        } catch (IOException | MimeTypeException e) {
            throw new RuntimeException(e);
        }

        return mimeType;
    }
}
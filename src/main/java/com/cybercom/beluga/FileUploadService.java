package com.cybercom.beluga;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Path("/beluga")
public class FileUploadService {
    private static final Log LOGGER = LogFactory.getLog(FileUploadService.class);
    private static final String APPLICATION_DEPLOY_PATH = "apps/";

    @DELETE
    @Path("/undeploy/{applicationName}")
    public Response unDeploy(@PathParam("applicationName")final String applicationName) {
        final String suffix = "-anchor.txt";
        String fileName = buildFileName(applicationName);
        fileName += suffix;
        File file = new File(fileName);
        try {
            file.delete();
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            LOGGER.error("Could not undeploy application=" + applicationName, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @POST
    @Path("/deploy")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@FormDataParam("file") final InputStream inputStream,
                               @FormDataParam("file") final FormDataContentDisposition fileDetail) {
        String fileName = fileDetail.getFileName();
        String fileLocation = buildFileName(fileName);
        LOGGER.info("fileLocation=" + fileLocation);

        String outputPath = writeToFile(inputStream, fileLocation);

        return Response.status(200).entity("successfully uploaded to " + outputPath).build();
    }

    private String buildFileName(final String fileName) {
        return APPLICATION_DEPLOY_PATH + fileName;
    }

    private String writeToFile(final InputStream uploadedInputStream, final String uploadedFileLocation) {
        OutputStream out = null;
        File file = new File(uploadedFileLocation);
        LOGGER.info("file abs pos=" + file.getAbsolutePath());
        try {

            out = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null)
                try {
                    uploadedInputStream.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return file.getAbsolutePath();
    }
}

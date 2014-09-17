package com.cybercom.beluga;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * User: Oskar Ferm <oskar.ferm@cybercom.com>
 * Date: 6/12/13
 * Time: 9:31 AM
 */
public class FileUploadServiceTest {
    private WebResource webResource;
    private static final String ADDRESS = "http://localhost:8082/beluga/deploy";

    @Before
    public void setUp() throws Exception {
        webResource = Client.create().resource(ADDRESS);
    }

    @Test
    @Ignore
    public void testUploadFile() throws Exception {
        String fileLocation = getClass().getResource("/temp.zip").getFile();
        File file = new File(fileLocation);
        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
        FileDataBodyPart fdp = new FileDataBodyPart("file", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        formDataMultiPart.bodyPart(fdp);
        String reString = webResource.type(MediaType.MULTIPART_FORM_DATA).accept(MediaType.TEXT_HTML)
                .post(String.class, formDataMultiPart);
        System.out.println(reString);
        System.out.println("Output from Server .... \n");
    }
}

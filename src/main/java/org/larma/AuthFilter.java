package org.larma;

import java.io.IOException;
import java.util.Base64;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static String bruker = "bruker";
    private static String passord  = "passord";

    @Override
    public void filter(
            final ContainerRequestContext requestContext
            ) throws IOException 
    {
        String auth = requestContext.getHeaderString("Authorization");
        if (auth == null || "".equals(auth)) {
            fail(requestContext, "Mangler autorisasjonsmerke");
            return;
        }
        String decodedAuth = new String(Base64.getDecoder().decode(auth));
        if (decodedAuth.endsWith(System.lineSeparator())) {
            decodedAuth = decodedAuth.substring(0, decodedAuth.length() - 1);
        }
        String[] up = decodedAuth.split(":");
        if (up.length != 2) {
            fail(requestContext, "Ugyldig bruker eller passord");
            return;
        }
        if (!up[0].equalsIgnoreCase(bruker) || !up[1].equalsIgnoreCase(passord)) {
            fail(requestContext, "Feil bruker eller passord");
            return;
        }
    }

    private void fail(
            final ContainerRequestContext requestContext,
            final String message) 
    {
        requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(message)
                    .build());
    }
    
    public static void setPassord(
            final String nyttPassord) 
    {
        passord = nyttPassord;
    }
}

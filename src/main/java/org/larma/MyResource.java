package org.larma;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Response;

@Path("/")
public class MyResource {
    private static final String[] tekst = {
        "Dette er null",
        "Dette er en",
        null,
        "Dette er tre"
    };
    private static final int[] sekvens = { 0, 1, 3 };
    private static final Random random = new Random(System.currentTimeMillis());

    @GET
    @Produces("image/png")
    @Path("favicon.ico")
    public String favIcon()
    {
        return null;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("test")
    public String getIt() {
        return "Hello, Heroku!";
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("{side}")
    public String getSide(
        @PathParam("side") final int side)
    {
        return lesFil(side);
    }

    private String lesFil(
        final int side) 
    {
        if (side < 0 || side >= tekst.length) {
            throw new WebApplicationException("Ugyldig side", Response.SC_NOT_FOUND);
        }
        String s =tekst[side];
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("<html><body>");
        sb.append("<h1>Side ").append(side).append("</h1>");
        sb.append("<p>");
        sb.append(s);
        sb.append("<p>");
        int i = forrige(side);
        if (i >= 0) {
            sb.append("<a href=\"").append(i).append("\">Forrige side</a><br>");
        }
        i = neste(side);
        if (i >= 0) {
            sb.append("<a href=\"").append(neste(side)).append("\">Neste side</a><br>");
        }
        //if (random.nextInt(2) == 1) {
            i = random.nextInt(tekst.length);
            sb.append("<a href=\"").append(i).append("\">Tilfeldig side</a><br>");
        //}

        sb.append("</body></html>");
        return sb.toString();
    }

    private int neste(
        final int side) 
    {
        int indeks = finn(side) + 1;
        return side(indeks);
    }

    private int forrige(
        final int side) 
    {
        int indeks = finn(side) - 1;
        return side(indeks);
    }

    private int side(
        final int indeks) 
    {
        if (indeks < 0 || indeks >= sekvens.length) {
            return -1;
        }
        return sekvens[indeks];
    }

    private int finn(
        final int side) 
    {
        for (int i = 0; i < sekvens.length; i++) {
            if (sekvens[i] == side) {
                return i;
            }
        }
        return -1;
    }
}

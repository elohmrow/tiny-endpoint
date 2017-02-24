/**
 * This file Copyright (c) 2015-2016 Magnolia International
 * Ltd.  (http://www.magnolia-cms.com). All rights reserved.
 *
 *
 * This file is dual-licensed under both the Magnolia
 * Network Agreement and the GNU General Public License.
 * You may elect to use one or the other of these licenses.
 *
 * This file is distributed in the hope that it will be
 * useful, but AS-IS and WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, or NONINFRINGEMENT.
 * Redistribution, except as permitted by whichever of the GPL
 * or MNA you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or
 * modify this file under the terms of the GNU General
 * Public License, Version 3, as published by the Free Software
 * Foundation.  You should have received a copy of the GNU
 * General Public License, Version 3 along with this program;
 * if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * 2. For the Magnolia Network Agreement (MNA), this file
 * and the accompanying materials are made available under the
 * terms of the MNA which accompanies this distribution, and
 * is available at http://www.magnolia-cms.com/mna.html
 *
 * Any modifications to this file must keep this entire header
 * intact.
 *
 */
package info.magnolia.training.tinyendpoint;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.wrapper.JCRMgnlPropertiesFilteringNodeWrapper;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.rest.AbstractEndpoint;
import info.magnolia.rest.registry.ConfiguredEndpointDefinition;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import info.magnolia.training.tinyendpoint.TinyEndpoint.Definition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Endpoint for retrieving, downloading and deleting cache entries.
 */
@Path("/custom/v1")
public class TinyEndpoint extends AbstractEndpoint<Definition> {


    @Inject
    public TinyEndpoint(Definition endpointDefinition) {
        super(endpointDefinition);
    }

    @GET
    @Path("/ping")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Checks if the service is alive")
    public Response ping() {
        return Response.ok().build();
    }

    @GET
    @Path("/ying")
    @Produces("image/jpg")
    @ApiOperation(value = "Checks if the service is alive")
    public byte[] ying() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(this.getClass().getResourceAsStream("ying"), baos);
        return baos.toByteArray();
    }

    @GET
    @Path("/propsof{path:(/.+)?}")
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "gets some data")
    public Map<String, String> read(@PathParam("path") @DefaultValue("/") String path) {
        Map<String, String> results = new HashMap<>();
        try {
            PropertyIterator iter = new JCRMgnlPropertiesFilteringNodeWrapper(MgnlContext.getSystemContext().getJCRSession(RepositoryConstants.WEBSITE).getNode(path)).getProperties();
            while (iter.hasNext()) {
                Property next = iter.nextProperty();
                results.put(next.getName(), next.getString());
            }
        } catch (RepositoryException e) {

        }
        return results;
    }

    /**
     * Definition for enclosing type.
     */
    public static class Definition extends ConfiguredEndpointDefinition {

    }
}

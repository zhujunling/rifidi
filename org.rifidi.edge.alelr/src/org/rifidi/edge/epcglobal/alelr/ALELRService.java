package org.rifidi.edge.epcglobal.alelr;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.1.4
 * 2015-12-12T10:17:43.260-05:00
 * Generated source version: 3.1.4
 * 
 */
@WebServiceClient(name = "ALELRService", 
                  wsdlLocation = "/src/org/rifidi/edge/alelr/xsd/EPCglobal-ale-1_1-alelr.wsdl",
                  targetNamespace = "urn:epcglobal:alelr:wsdl:1") 
public class ALELRService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("urn:epcglobal:alelr:wsdl:1", "ALELRService");
    public final static QName ALELRServicePort = new QName("urn:epcglobal:alelr:wsdl:1", "ALELRServicePort");
    static {
        URL url = ALELRService.class.getResource("/src/org/rifidi/edge/alelr/xsd/EPCglobal-ale-1_1-alelr.wsdl");
        if (url == null) {
            url = ALELRService.class.getClassLoader().getResource("/src/org/rifidi/edge/alelr/xsd/EPCglobal-ale-1_1-alelr.wsdl");
        } 
        if (url == null) {
            java.util.logging.Logger.getLogger(ALELRService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/src/org/rifidi/edge/alelr/xsd/EPCglobal-ale-1_1-alelr.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public ALELRService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ALELRService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ALELRService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public ALELRService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public ALELRService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public ALELRService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns ALELRServicePortType
     */
    @WebEndpoint(name = "ALELRServicePort")
    public ALELRServicePortType getALELRServicePort() {
        return super.getPort(ALELRServicePort, ALELRServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ALELRServicePortType
     */
    @WebEndpoint(name = "ALELRServicePort")
    public ALELRServicePortType getALELRServicePort(WebServiceFeature... features) {
        return super.getPort(ALELRServicePort, ALELRServicePortType.class, features);
    }

}

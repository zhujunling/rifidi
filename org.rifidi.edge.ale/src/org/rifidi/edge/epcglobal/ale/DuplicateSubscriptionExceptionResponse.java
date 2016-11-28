
package org.rifidi.edge.epcglobal.ale;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.4
 * 2015-12-12T14:01:05.543-05:00
 * Generated source version: 3.1.4
 */

@WebFault(name = "DuplicateSubscriptionException", targetNamespace = "urn:epcglobal:ale:wsdl:1")
public class DuplicateSubscriptionExceptionResponse extends Exception {
    
    private org.rifidi.edge.epcglobal.ale.DuplicateSubscriptionException duplicateSubscriptionException;

    public DuplicateSubscriptionExceptionResponse() {
        super();
    }
    
    public DuplicateSubscriptionExceptionResponse(String message) {
        super(message);
    }
    
    public DuplicateSubscriptionExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSubscriptionExceptionResponse(String message, org.rifidi.edge.epcglobal.ale.DuplicateSubscriptionException duplicateSubscriptionException) {
        super(message);
        this.duplicateSubscriptionException = duplicateSubscriptionException;
    }

    public DuplicateSubscriptionExceptionResponse(String message, org.rifidi.edge.epcglobal.ale.DuplicateSubscriptionException duplicateSubscriptionException, Throwable cause) {
        super(message, cause);
        this.duplicateSubscriptionException = duplicateSubscriptionException;
    }

    public org.rifidi.edge.epcglobal.ale.DuplicateSubscriptionException getFaultInfo() {
        return this.duplicateSubscriptionException;
    }
}

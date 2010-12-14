/** 
 * NceMonFrame.java
 *
 * Description:		Frame displaying (and logging) NCE command messages
 * @author			Bob Jacobsen   Copyright (C) 2001
 * @version			
 */

package jmri.jmrix.nce.ncemon;

import jmri.jmrix.nce.NceListener;
import jmri.jmrix.nce.NceTrafficController;
import jmri.jmrix.nce.NceMessage;
import jmri.jmrix.nce.NceReply;

@Deprecated
public class NceMonFrame extends jmri.jmrix.AbstractMonFrame implements NceListener {

    private NceTrafficController tc = null;
    
	public NceMonFrame(NceTrafficController t) {
		super();
		this.tc = t;
	}

	protected String title() { return "NCE Command Monitor"; }
	
	protected void init() {
		// connect to TrafficController
		tc.addNceListener(this);
	}
  
	public void dispose() {
		tc.removeNceListener(this);
		super.dispose();
	}
			
	public synchronized void message(NceMessage l) {  // receive a message and log it
        if (l.isBinary())
          	nextLine("binary cmd: "+l.toString()+"\n", null);
        else
            nextLine("cmd: \""+l.toString()+"\"\n", null);

	}

	public synchronized void reply(NceReply l) {  // receive a reply message and log it
	    String raw = "";
	    for (int i=0;i<l.getNumDataElements(); i++) {
	        if (i>0) raw+=" ";
            raw = jmri.util.StringUtil.appendTwoHexFromInt(l.getElement(i)&0xFF, raw);
        }
	        
	    if (l.isUnsolicited()) {    
            nextLine("msg: \""+l.toString()+"\"\n", raw);
        } else {
            nextLine("rep: \""+l.toString()+"\"\n", raw);
        }
	}
	
   static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NceMonFrame.class.getName());

}

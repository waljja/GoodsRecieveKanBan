

package ht.util;


import java.io.File;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import sun.misc.BASE64Encoder;


public class EmailcontrolByAdmin
{

    public boolean sendMail(String subject,String sendaddr, String sendto,String sendcc,String main)
    { 
    	 boolean b=true;
         StringTokenizer stringtokenizer = new StringTokenizer(sendto, ";");
         StringTokenizer stringtokenizer1 = new StringTokenizer(sendcc, ";");
         String s15 = "smtp.honortone.com";
         int i = 0;
         int j = 0;
         try
         {
             boolean flag = false;
             Properties properties = System.getProperties();
             properties.put("mail.host", s15);
             properties.put("mail.transport.protocol", "smtp");
             properties.put("mail.smtp.auth", "true");
             javax.mail.Session session = Session.getDefaultInstance(properties, null);
             MimeMessage mimemessage = new MimeMessage(session);
             mimemessage.setFrom(new InternetAddress(sendaddr));
             InternetAddress ainternetaddress[] = new InternetAddress[stringtokenizer.countTokens()];
             InternetAddress ainternetaddress1[] = new InternetAddress[stringtokenizer1.countTokens()];
             while(stringtokenizer.hasMoreTokens()) 
             {
                 ainternetaddress[i] = new InternetAddress(stringtokenizer.nextToken());
                 i++;
             }
             while(stringtokenizer1.hasMoreTokens()) 
             {
                 ainternetaddress1[j] = new InternetAddress(stringtokenizer1.nextToken());
                 j++;
             }
             mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, ainternetaddress);
             mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, ainternetaddress1);
             BASE64Encoder base64encoder = new BASE64Encoder();
             mimemessage.setSubject("=?utf-8?B?" + base64encoder.encode(subject.getBytes("utf-8")) + "?=");
             mimemessage.setSentDate(new java.util.Date());
             mimemessage.setContent(main, "text/html;charset=utf-8");
             session.setDebug(flag);
             Transport transport = session.getTransport("smtp");
             //transport.connect(s15, "shengwen.zhang@honortone.com", "smiles3177");
             transport.connect(s15, "system messenger", "be587446");
             transport.sendMessage(mimemessage, mimemessage.getAllRecipients());
             transport.close();
         }
         catch(Exception exception) {
        	 exception.printStackTrace();
         }
         return b;
     }

    /**
     * 
     * @param email_from
     * @param email_to
     * @param email_cc
     * @param email_bcc
     * @param email_subject
     * @param email_body
     * @param attachment
     * @param request
     * //?????????????????????    
  	//???????????????:?????????,?????????,?????????,?????????,??????,????????????,????????????+?????????(ArrayList)
     */
    
    public boolean sendMail2(String subject, String sendaddr, String sendto, String sendcc, String main)
    {
      boolean b = true;
      StringTokenizer stringtokenizer = new StringTokenizer(sendto, ";");
      StringTokenizer stringtokenizer1 = new StringTokenizer(sendcc, ";");

      String s15 = "smtp.honortone.com";

      int i = 0;
      int j = 0;
      try
      {
        boolean flag = false;
        Properties properties = System.getProperties();
        properties.put("mail.host", s15);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(properties, null);
        MimeMessage mimemessage = new MimeMessage(session);
        mimemessage.setFrom(new InternetAddress(sendaddr));
        InternetAddress[] ainternetaddress = new InternetAddress[stringtokenizer.countTokens()];
        InternetAddress[] ainternetaddress1 = new InternetAddress[stringtokenizer1.countTokens()];
        while (stringtokenizer.hasMoreTokens())
        {
          ainternetaddress[i] = new InternetAddress(stringtokenizer.nextToken());
          i++;
        }
        while (stringtokenizer1.hasMoreTokens())
        {
          ainternetaddress1[j] = new InternetAddress(stringtokenizer1.nextToken());
          j++;
        }
        mimemessage.setRecipients(Message.RecipientType.TO, ainternetaddress);
        mimemessage.setRecipients(Message.RecipientType.CC, ainternetaddress1);
        BASE64Encoder base64encoder = new BASE64Encoder();
        mimemessage.setSubject(subject, "text/html;charset=utf-8");
        mimemessage.setSentDate(new Date());
        mimemessage.setContent(main, "text/html;charset=utf-8");
        session.setDebug(flag);
        Transport transport = session.getTransport("smtp");
        transport.connect(s15, "shengwen.zhang@honortone.com", "smiles3177");
        transport.sendMessage(mimemessage, mimemessage.getAllRecipients());
        transport.close();
      }
      catch (Exception exception) {
        exception.printStackTrace();
      }
      return b;
    }
    //alan
    public String sendMailWidthAttach(String email_from,String email_to,String email_cc,String email_bcc,String email_subject,String email_body,ArrayList attachment) 

    {
    	 boolean b=true;
         String s15 = "smtp.honortone.com";
         int j = 0;
    	  ArrayList vec = attachment;
    	  try
    	  {    	
    		//logger.info("==send email enter==");
    		boolean sessionDebug=false;
      	java.util.Properties props=System.getProperties();
      	props.put("mail.host", s15);
      	props.put("mail.transport.protocol","smtp");
      	props.put("mail.smtp.auth","true");
      	//props.put("mail.smtp.port",smtpport);
      	//
      	Session mailSession=Session.getDefaultInstance(props,null);
      	MimeMessage msg=new MimeMessage(mailSession);
      	msg
	      .setFrom(new InternetAddress("\""+ MimeUtility.encodeText("Admin System Message")   //????????????????????????
+ "\" <"+email_from+">"));
      	//msg.setFrom(new InternetAddress(email_from));
      	//======================================????????????????????????,??????,???????????????==============================================
    		//to???????????????
      	if(!email_to.equals(""))
      	{
    		    StringTokenizer to_kenizer = new StringTokenizer(email_to,";") ;
    			while(to_kenizer.hasMoreTokens())
    			{	
    					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to_kenizer.nextToken()));
    			}
    		}
    		//???????????????
      	//	message.addRecipient(Message.RecipientType.BCC, new InternetAddress(cc));	//?????? ??????
      	if(!email_bcc.equals(""))
      	{
    			StringTokenizer bcc_kenizer = new StringTokenizer(email_bcc,";") ;
    			while(bcc_kenizer.hasMoreTokens())
    			{	
    					msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc_kenizer.nextToken()));
    			}
    		}
    		//cc???????????????
    		String tempOut="";
    		int countAdress=0;
    		Vector vecEmailCc=new Vector();
    		vecEmailCc.clear();
    		if(!email_cc.equals(""))
      	{
    			StringTokenizer cc_kenizer = new StringTokenizer(email_cc,";") ;
    			countAdress=0;
    			while(cc_kenizer.hasMoreTokens())
    			{	
    				  tempOut=cc_kenizer.nextToken();
    				  vecEmailCc.add(tempOut);
    				  countAdress++;
    			}
    			Address[] ccAddress =new Address[countAdress];
    		    for(int i=0;i<countAdress;i++)
    			{
    				ccAddress[i]=new InternetAddress(vecEmailCc.get(i).toString());
    			}
    			msg.addRecipients(Message.RecipientType.CC,ccAddress);
    		}		
    	    //======================================??????????????????,????????????====================================================				
    		sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
    		//msg.setSubject("=?utf-8?B?"+enc.encode(email_subject.getBytes("utf-8"))+"?="); //?????????????????????????????????2017-07-28
    		msg.setSubject(MimeUtility.encodeText(email_subject, "UTF-8", "B"));
    		msg.setSentDate(new java.util.Date());
    		BodyPart messageBodyPart0 = new MimeBodyPart();
    		//========================================???????????????==================================================
    		// add attachment
    		/*logger.info("email_body="+email_body);*/
    	    String htmlText =crtoB(email_body)+ "<br>";
    	    //logger.info("htmlText="+htmlText);
    		messageBodyPart0.setContent(htmlText,"text/html;charset=GBK");
    	    //messageBodyPart0.setContent(enc.encode(htmlText.getBytes("utf-8")),"text/html;charset=GBK");
    		MimeMultipart multipart = new MimeMultipart("related");
    		multipart.addBodyPart(messageBodyPart0);
    		/*MimeMultipart multipart = new MimeMultipart();
    		messageBodyPart0.setText(email_body);
    		messageBodyPart0.setContent(email_body, "text/html;charset=utf-8");
    		multipart.addBodyPart(messageBodyPart0);*/

    		//logger.info("=email att  info==");
    		//====================================?????????????????????=========================================================
    		//attachment part
    		BodyPart messageBodyPart = new MimeBodyPart();
    		String file="";	
    		Iterator iter=vec.iterator();
    		while(iter.hasNext())
    		{   
    			file = iter.next().toString();
    			File Att_File = new File(file);
    			if(!Att_File.exists())
    			{
    				//System.out.println("Sorry,error! The file  "+file+"  not found,fail in sending!");
    				//logger.info("Sorry,error! The file !");
    				return "nofile";
    			}
    			messageBodyPart = new MimeBodyPart();
    			DataSource source = new FileDataSource(file);
    			messageBodyPart.setDataHandler(new DataHandler(source));
    			
    			String filnames=file.substring(file.lastIndexOf("/")+1);//???????????????
    			String filname=filnames.substring(filnames.indexOf("_")+1);//????????????id_
    			//System.out.println("Emailcontrol:"+filname);
    			//messageBodyPart.setFileName("=?utf-8?B?"+enc.encode(filname.getBytes("utf-8"))+"?=");
    			messageBodyPart.setFileName(MimeUtility.encodeText(filname));
    			multipart.addBodyPart(messageBodyPart);
    		}	
    		//logger.info("=email att  info   end==");
    		//end add attachment
    		try{
  	  		msg.setContent(multipart);
  	   		mailSession.setDebug(sessionDebug);
  	   		Transport transport = mailSession.getTransport("smtp");  	   		
  	   		transport.connect(s15, "system messenger", "be587446");
  	  		transport.sendMessage(msg,msg.getAllRecipients());
  	  		transport.close();
    		}catch(Exception e){
    			e.printStackTrace();
    	  	    return e.toString();
    	  	} 
    	}
    	catch(Exception e)
    	{
    		//System.out.println(e.toString());
    		e.printStackTrace();
    	    return e.toString();
    	}  
    	return "success";
    }
    public  String crtoB(String strCon) {

		  int SPos = 0; //????????????
		  int EPos = 0; //??????????????????
		  int totallen = strCon.length();//??????????????????
		  String strShow = "";//????????????????????????
		  String strMid = "";

	  	  for(EPos=0;EPos<totallen;EPos++) {

	    	char c = strCon.charAt(EPos);

	    	if(c==13) //???????????????????????????

	    	{
	    	  if(EPos!=SPos) {

	    	    strMid = strCon.substring(SPos,EPos);
	    	    strShow = strShow + strMid + "<br>";

	    	  }
	          else {

	       		 strShow = strShow + " ";

			     }
	         SPos = EPos +2;//???SPos???????????????????????????????????????

	        }//c==13

	       }//end for

	      if(SPos<EPos){ //??????????????????????????????

	         strMid =strCon.substring(SPos,EPos);
	         strShow = strShow +strMid +" ";
	      }

	      return strShow; //??????strShow??????.

	   }
    
    public static void main(String[] args) {
    	EmailcontrolByAdmin e =new EmailcontrolByAdmin();
    	//e.sendMail("test", "shengwen.zhang@honortone.com", "shengwen.zhang@honortone.com", "", "test");
    	String myfilePath = "C:/TPGDATA/steve.csv";
    	ArrayList attachment=new ArrayList();
    	attachment.add(myfilePath);
		//email.sendMail("TPG???????????????", "shengwen.zhang@honortone.com", "shengwen.zhang@honortone.com", "", "TPG???????????????,?????????????????????<a href='http://app02.honortone.com:8091/acso/'>login</a>");
		//e.sendMailWidthAttach("shengwen.zhang@honortone.com", "shengwen.zhang@honortone.com", "", "", "TPG???????????????", "TPG???????????????,?????????????????????<a href='http://app02.honortone.com:8091/acso/'>login</a>", attachment);
		e.sendMailWidthAttach("huiming.liu@honortone.com", "huiming.liu@honortone.com", "", "", "TPG New PO File Email Alert", "&nbsp;&nbsp;&nbsp;&nbsp;Login to the <a href='http://app02.honortone.com:8091/acso/'>acso</a> system check the details.", attachment);
	}
	
   
}
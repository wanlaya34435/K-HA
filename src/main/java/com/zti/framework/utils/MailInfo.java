package com.zti.framework.utils;

import java.util.HashMap;

public class MailInfo {
	String mailHostname;
	String mailUsername;
	String mailPassword;
	String mailTo;
	String port;
	
	String msgTemplateLocation;
//	String msgURL;
	String msgSubject;

    String mailSender;

    public String getMailSender() {
        return mailSender;
    }

    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    private boolean ttls;
    private boolean auth;

    private HashMap<String, String> paramsMap;

    public boolean isTtls() {
        return ttls;
    }

    public void setTtls(boolean ttls) {
        this.ttls = ttls;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public HashMap<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(HashMap<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getMailHostname() {
		return mailHostname;
	}

	public void setMailHostname(String mailHostname) {
		this.mailHostname = mailHostname;
	}

	public String getMailUsername() {
		return mailUsername;
	}

	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMsgTemplateLocation() {
		return msgTemplateLocation;
	}

	public void setMsgTemplateLocation(String msgTemplateLocation) {
		this.msgTemplateLocation = msgTemplateLocation;
	}


	public String getMsgSubject() {
		return msgSubject;
	}

	public void setMsgSubject(String msgSubject) {
		this.msgSubject = msgSubject;
	}
}

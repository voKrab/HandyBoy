package com.vallverk.handyboy.server;

import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class MySSLSocketFactory extends SSLSocketFactory
{
	SSLContext sslContext = SSLContext.getInstance ( "TLS" );

	public MySSLSocketFactory ( KeyStore truststore ) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
	{
		super ( truststore );

		TrustManager tm = new X509TrustManager ()
		{
			@Override
			public void checkClientTrusted ( java.security.cert.X509Certificate[] arg0, String arg1 ) throws java.security.cert.CertificateException
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted ( java.security.cert.X509Certificate[] arg0, String arg1 ) throws java.security.cert.CertificateException
			{
				// TODO Auto-generated method stub

			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers ()
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
		sslContext.init ( null, new TrustManager[] { tm }, null );
	}

	@Override
	public Socket createSocket ( Socket socket, String host, int port, boolean autoClose ) throws IOException, UnknownHostException
	{
		return sslContext.getSocketFactory ().createSocket ( socket, host, port, autoClose );
	}

	@Override
	public Socket createSocket () throws IOException
	{
		return sslContext.getSocketFactory ().createSocket ();
	}

}
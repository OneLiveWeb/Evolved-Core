package org.netevolved.sync;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.openedit.page.Page;
import org.openedit.util.HttpMimeBuilder;
import org.openedit.util.HttpSharedConnection;

import java.io.File;

public class EmUploader
{
	HttpSharedConnection httpconnection;
	OutputFiller fieldFiller;

	public OutputFiller getFiller()
	{
		if (fieldFiller == null)
		{
			fieldFiller = new OutputFiller();
		}
		return fieldFiller;
	}

	public void setFiller(OutputFiller inFiller)
	{
		fieldFiller = inFiller;
	}

	public HttpSharedConnection getHttpConnection()
	{
		if (httpconnection == null)
		{
			httpconnection = new HttpSharedConnection();
		}

		return httpconnection;
	}



//	public boolean login(URI uri, String server, String inUname, String inKey)
//	{
//		String path = getConfig().get("home");
//		if (path == null)
//		{
//			path = findHome();
//			getConfig().put("home", path);
//		}
//		getConfig().put("username", inUname);
//		getConfig().put("server", server);
//		//check  for key
//		getConfig().put("key", inKey);
//		getConfig().save();
//		//Check disk space etc?
////		try
////		{
////
////			log("Connecting to " + uri);
////
////			boolean ok = getConnection().connect();
////			if (!ok)
////			{
////				log("Could not connect to " + getConnection().getURI());
////				return false;
////			}
////			log("Connected to " + getConnection().getURI());
////		}
////		catch (InterruptedException e)
////		{
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////			log("Error connecting to " + getConnection().getURI());
////			return false;
////		}
//		Message mes = new Message("login");
//		mes.put("home", path);
//		mes.put("username", inUname);
//		mes.put("server", server);
//		mes.put("entermedia.key", inKey);
//		mes.put("desktopid", System.getProperty("os.name") + " " + getComputerName());
//		mes.put("homefolder", path);
//
//		Collection checkedout = new ArrayList();
//		//load up
//		File home = new File(getWorkFolder());
//		home.mkdirs();
//		File[] found = home.listFiles();
//		for (int i = 0; i < found.length; i++)
//		{
//			File item = found[i];
//			if( item.isDirectory() )
//			{
//				checkedout.add(item.getName());
//			}
//		}
//		mes.put("existingcollections", checkedout);
//		getConnection().send(mes);
//		return true;
//
//	}


	private void log(String inString)
	{
		System.out.println(inString);
	}






	private void debug(String inString)
	{
		System.out.println(inString);
	}





//	public String downloadFile(JSONObject inMap)
//	{
//
//		String url = (String) inMap.get("url");
//		String assetid = (String) inMap.get("assetid");
//		String catalogid = (String) inMap.get("catalogid");
//		String filename = (String) inMap.get("filename");
//		String mediadb = (String) inMap.get("mediadbid");
//
//		String finalurl = getServer() +"/"+ mediadb + "/services/module/asset/downloads/originals/" + url;
//		String path = getWorkFolder() + "/assets/" + catalogid + "/" + assetid + "/" + filename ;
//		File folder = new File(path);
//		folder.getParentFile().mkdirs();
//		inMap.put("entermedia.key", getEnterMediaKey());
//
//		HttpResponse resp = getHttpConnection().sharedTextPost(finalurl, inMap);
//		if (resp.getStatusLine().getStatusCode() == 200)
//		{
//			InputStream input = null;
//			FileOutputStream output = null;
//			try
//			{
//				input = resp.getEntity().getContent();
//				folder.getParentFile().mkdirs();
//				output = new FileOutputStream(folder);
//				getFiller().fill(input, output);
//
//				String savetime = (String) inMap.get("assetmodificationdate");
//				folder.setLastModified(Long.parseLong(savetime));
//
//			}
//			catch (Throwable ex)
//			{
//				getLogListener().reportError("Problem downloading", ex);
//			}
//			finally
//			{
//				getFiller().consume(resp);
//				getFiller().close(input);
//				getFiller().close(output);
//			}
//
//		}
//		else
//		{
//			getLogListener().info(resp.getStatusLine().getStatusCode() + " Could not download " + url + " " + resp.getStatusLine().getReasonPhrase());
//		}
//
//		return path;
//
//	}

	public void uploadAsset(String entermediakey, String inRemoteServer, Page inPage, JSONObject inMap)
	{
		// TODO Auto-generated method stub


		getHttpConnection().addSharedHeader("entermedia.key", entermediakey);
	//	getHttpConnection().addSharedCookie(host,"entermedia.key", entermediakey);
	
		try
		{
			//String url = (String) inMap.get("uploadurl");
					
			String mediadbid = (String)inMap.get("mediadb");
			String assetid = (String) inMap.get("assetid");
			String filepath = inPage.getContentItem().getAbsolutePath();
			
			String url = null;



				url = inRemoteServer + "/" + mediadbid + "/services/module/asset/create?entermedia.key="+entermediakey;

			File file = new File(filepath);
			HttpMimeBuilder builder = new HttpMimeBuilder();

			//TODO: Use HttpRequestBuilder.addPart()
			HttpPost method = new HttpPost(url);
			method.addHeader("entermedia.key", entermediakey);
			//POST https://www.googleapis.com/upload/storage/v1/b/myBucket/o?uploadType=multipart
			builder.addPart("metadata", inMap.toJSONString(), "application/json"); //What should this be called?
			builder.addPart("file.0", file);
			builder.addPart("path", url);

			method.setEntity(builder.build());

			HttpResponse resp = getHttpConnection().getSharedClient().execute(method);

			if (resp.getStatusLine().getStatusCode() != 200)
			{
				String returned = EntityUtils.toString(resp.getEntity());

			}
			
			
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}
	
	
	
	
}

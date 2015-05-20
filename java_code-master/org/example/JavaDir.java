package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.model.Repository;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.ContentAccessor;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.site.SiteInfo;
import org.alfresco.service.cmr.site.SiteService;
import org.alfresco.service.namespace.QName;
import org.apache.chemistry.opencmis.commons.impl.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.extensions.surf.util.Content;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.extensions.webscripts.WrappingWebScriptRequest;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;
import org.springframework.extensions.webscripts.servlet.WebScriptServletRequest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.Image;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class JavaDir extends DeclarativeWebScript
{	 protected ServiceRegistry serviceRegistry;
protected ContentService contentService;
protected SiteService siteService;
protected NodeService nodeService;
protected FileFolderService fileFolderService;
protected NodeRef parentNode;
private InputStream is;	
File ttfile;
File txt;
File pdf ;
String result ;
String filename;
private Repository repository;
String dropdown;
//private SiteInfo site;
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
	       this.serviceRegistry = serviceRegistry;
	 }
	 
	public void setRepository(Repository repository)
	{
		this.repository = repository;
	}
//	public void setSiteService(SiteService siteService) {
//	       this.siteService = siteService;
//	 }
	 
	protected Map<String, Object> executeImpl(WebScriptRequest req,
			Status status, Cache cache)
			{
 		 dropdown =req.getParameter("myname");
		 System.setProperty("jna.library.path", "64".equals(System.getProperty("sun.arch.data.model")) ? "lib/libtesseract.so" : "lib/libtesseract.so");
		 this.contentService = serviceRegistry.getContentService();
		  this.nodeService = serviceRegistry.getNodeService();
		  this.fileFolderService = serviceRegistry.getFileFolderService();
		  LinkedHashSet<String> nodes = new LinkedHashSet<String>();
		  String split1=dropdown.substring(dropdown.indexOf("param="));		
		String split2=split1.substring(0,split1.indexOf("param2"));
		String split1b=split1.substring(split1.indexOf("param2"));
		String split2b=split1b.substring(split1b.indexOf("foldername="));
		  String site_name=split1b.substring(split1b.indexOf("foldername="),split1b.indexOf("&"));
		String final_name=site_name.replaceAll("foldername=","");
		  String inner = split2.replaceAll("param=", "");
		String innerf=inner.replaceAll("documentLibrary", "");		  
		String inner_arr[]=inner.split("/");
		          String outer_arr[]=split2b.split("%");
		        String filterchain="";
		          nodes.add("Sites");
				  nodes.add(final_name);
				  nodes.add("documentLibrary");

    for(String outer:outer_arr){
		        	  
		        	  for(String inner1:inner_arr){
		        		  if(outer.contains(inner1)){
		        			  if(!inner1.equalsIgnoreCase("&")){
                               nodes.add(inner1);
		        			  }  
		        		  }
		        		  
		        		  
		        	  }
		        	  
		        	  
		        	  
		          }
		          
		          
		  //nodes.add("test");
 		  NodeRef companyhome=repository.getCompanyHome();
		  NodeRef parentNode;
		  NodeRef doclib;
 		  try {
		//	doclib=siteService.getContainer(site.getShortName(),"documentlibrary");
  			  
 			  ArrayList<String> final_nodes = new ArrayList<String>(nodes);

 			  
 			  parentNode = serviceRegistry.getFileFolderService().resolveNamePath(companyhome, final_nodes).getNodeRef();
String uul=req.getURL();
			File urlf=new File("/home/raghavbhardwaj/Documents/url.txt");
			try {
				urlf.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				FileWriter fwd=new FileWriter(urlf);
				//fwd.append(parentNode.toString());
 				fwd.append(site_name);

				fwd.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			if (parentNode != null) {
				
				 FormData formData = (FormData)req.parseContent();
				        FormData.FormField[] fields = formData.getFields();
  				   for(FormData.FormField field : fields) {
 				            if(field.getName().equals("file") && field.getIsFile()) {
				            try {
							
				            	ttfile=new File("/home/raghavbhardwaj/Documents/"+field.getFilename());
				            	ttfile.createNewFile();
				            	OutputStream outputStream = new FileOutputStream(ttfile);
org.apache.commons.io.IOUtils.copy(field.getInputStream(), outputStream);
outputStream.close();
				            } catch (java.io.FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				            	
				            	try {
									writeContent(field, parentNode,req);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				            }
				        } 
				  
		
		
		
		}
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
 		  
		 
		 
		return null;
		 }
			
		 protected NodeRef getNodeRef (String request) {
		  try {
		   return new NodeRef(request);
		  } catch (Exception e) {
		   System.out.println(e);
		   
		   return null;
		  }
		 }
		 

		 protected boolean writeContent (FormData.FormField field, NodeRef parentNode,WebScriptRequest req) throws IOException {
		  boolean success = false;

//		        Tesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
//		        instance.setDatapath("/usr/share/tesseract-ocr/tessdata");
//		       instance.setHocr(true);
//		        // Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping
//		  
//		      
//       
//		       
 		       

		  
		       
		       
		       
		       
		       
		       
		       
		       
//		        try {
//		             result = instance.doOCR(ttfile);
//                
// 		             txt=new File("/home/raghavbhardwaj/Documents/scanned.html");
//
//		             try {
//						txt.createNewFile();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		             FileWriter fw= new FileWriter(txt);
//		             fw.append(result);
//		             fw.close();
//		               pdf = new File("/home/raghavbhardwaj/Documents/data.pdf");
//		             Document pdfDocument = null;
//		             PdfWriter pdfWriter = null;
//		 
//
//		                 pdfDocument = new Document();
//		                 try {
//							pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdf));
//						} catch (DocumentException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//		                 pdfDocument.open();
//
//		                 XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, pdfDocument,
//		                         new FileInputStream(txt));
//		                pdfDocument.close();
//		        } catch (TesseractException e) {
//		            System.err.println(e.getMessage());
//		        } catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}  
//		    
//		        
//		        
//		        RandomAccessFileOrArray myTiffFile=new RandomAccessFileOrArray("/home/raghavbhardwaj/Documents/tt.tiff");
//		        //Find number of images in Tiff file
//		        int numberOfPages=TiffImage.getNumberOfPages(myTiffFile);
//		        System.out.println("Number of Images in Tiff File" + numberOfPages);
//		        Document TifftoPDF=new Document();
//		        try {
//					PdfWriter.getInstance(TifftoPDF, new FileOutputStream("/home/raghavbhardwaj/Documents/tiff2Pdf.pdf"));
//				} catch (DocumentException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//		        TifftoPDF.open();
//		        //Run a for loop to extract images from Tiff file
//		        //into a Image object and add to PDF recursively
//		        for(int i=1;i<=numberOfPages;i++){
//		            com.itextpdf.text.Image tempImage=TiffImage.getTiffImage(myTiffFile, i);
//		            try {
//						TifftoPDF.add(tempImage);
//						TifftoPDF.addKeywords(result);
//					} catch (DocumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		        }
//		        TifftoPDF.close();
// 		        System.out.println("Tiff to PDF Conversion in Java Completed" );
//		     
//		        
//		  System.setProperty("java.io.tmpdir", "/home/raghavbhardwaj/Documents");
//		    String tempdir=System.getProperty("java.io.tmpdir");    
		        
	
		     filename=field.getFilename();
			 String []s1=filename.split("\\.");
			 String s2=s1[0];
		     String attr_lang=req.getParameter("languages");
		     if(attr_lang.equalsIgnoreCase("English")){
		     Process p = Runtime.getRuntime().exec("tesseract "+"/home/raghavbhardwaj/Documents/"+filename+" "+"/home/raghavbhardwaj/Documents/"+s2+" pdf");
		    
		     
		     }else if(attr_lang.equalsIgnoreCase("Hindi")){
		    	 
		    	 
			     Process p = Runtime.getRuntime().exec("tesseract "+"/home/raghavbhardwaj/Documents/"+filename+" "+"/home/raghavbhardwaj/Documents/"+s2+" -l hin pdf");
 
		    	 
		     }else if(attr_lang.equalsIgnoreCase("Bangala")){
		    	 
		    	 
			     Process p = Runtime.getRuntime().exec("tesseract "+"/home/raghavbhardwaj/Documents/"+filename+" "+"/home/raghavbhardwaj/Documents/"+s2+" -l ben pdf");

		    	 
		    	 
		    	 
		     }
		     
		     
		     
		     
		     
		     File finalupload=new File("/home/raghavbhardwaj/Documents/"+s2+".pdf");
		        
		        
		        try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        
		        
		        
		        
		        
		        
		        InputStream targetStream = FileUtils.openInputStream(finalupload);				 
		        String fileName = finalupload.getName()+".pdf";
		        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();				       
		        String mimeType = "Adobe PDF Document";		        
		        
 				        		NodeRef newNode = createNewNode(parentNode, s2+".pdf");
		        
		        
		        
		        
		  try {
		   ContentWriter writer = contentService.getWriter(newNode, ContentModel.PROP_CONTENT, true);
		   writer.setMimetype(MimetypeMap.MIMETYPE_PDF);
		            writer.putContent(targetStream);
		            
		            success = true;
		  } catch (Exception e) {
		   System.out.println(e);
		  }
		        
		  return success;
		         }
		  

		 protected NodeRef createNewNode (NodeRef parentNode, String fileName) {
		  try {
		   QName contentQName = QName.createQName("{http://www.alfresco.org/model/content/1.0}content");
		   FileInfo newNodeRef = fileFolderService.create(parentNode, fileName, contentQName);
		   
		   return newNodeRef.getNodeRef();
		  } catch (Exception e) {
		   System.err.println(e);
		   
		   return null;
		  }
		  
		 }
		 
}
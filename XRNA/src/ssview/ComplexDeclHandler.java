package ssview;

public class
ComplexDeclHandler
implements org.xml.sax.ext.DeclHandler
{
   public void attributeDecl(java.lang.String elementName,
                             java.lang.String attributeName,
                             java.lang.String type,
                             java.lang.String valueDefault,
                             java.lang.String value)
   {

      System.out.println("ATTRIBUTE: ");
      System.out.println("Element Name: " + elementName);
      System.out.println("Attribute Name: " + attributeName);
      System.out.println("Type: " + type);
      System.out.println("Default Value: " + valueDefault);
      System.out.println("Value: " + value);
      System.out.println();
   }

   public void elementDecl(java.lang.String name,
                           java.lang.String model)
   {

      System.out.println("ELEMENT: ");
      System.out.println("Name: " + name);
      System.out.println("Model: " + model);
      System.out.println();
   }

   public void externalEntityDecl(java.lang.String name,
                                  java.lang.String publicId,
                                  java.lang.String systemId)
  {
     System.out.println("EXTERNAL ENTITY:");
     System.out.println("name: " + name);
     System.out.println("publicId: " + publicId);
     System.out.println("systemId: " + systemId);
  }

  public void internalEntityDecl(java.lang.String name,
                                 java.lang.String value)
  {
     System.out.println("INTERNAL ENTITY: " + name + value);
  }
}


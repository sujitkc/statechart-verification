import com.code_intelligence.jazzer.api.FuzzedDataProvider;
    import java.io.*;  
    class Depersist{  
     public static void main(String args[]){  
      try{  
      //Creating stream to read the object  
      ObjectInputStream in=new ObjectInputStream(new FileInputStream("crash-f52517a87a3c927fea075e938b800dfef6969c98"));  
      FuzzedDataProvider s=(FuzzedDataProvider)in.readObject();  
      //printing the data of the serialized object  
      System.out.println(s.toString());  
      //closing the stream  
      in.close();  
      }catch(Exception e){System.out.println(e);}  
     }  
    }  

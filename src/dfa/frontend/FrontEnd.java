package frontend;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.Scanner;

import ast.*;
//

public class FrontEnd {

  private final Parser parser;

  // constructors
  public FrontEnd(String input) throws FileNotFoundException 
  {
    this.parser = new Parser(new Lexer(new FileReader(input)));
  }

  // pasrser
  public Parser getParser() 
  {
    return this.parser;
  }
  
}
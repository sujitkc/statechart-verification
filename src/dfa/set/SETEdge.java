package set;

import utilities.IdGenerator;

public class SETEdge implements ISETEdge{

  SET mSET;
  SETNode mTail; // source
  SETNode mHead; // destination
  private String mId;
  
  public SETEdge(SET set, SETNode tail, SETNode head) {
    this.mSET = set;
    this.mTail = tail;
    this.mHead = head;
    this.mId = SETEdge.generateId();
  }

  public SETEdge(String id, SET set, SETNode tail, SETNode head) throws Exception {
    this.mSET = set;
    this.mTail = tail;
    this.mHead = head;

    if(IdGenerator.hasId(id)) {
      Exception e = new Exception("Can't construct SETEdge : something with name '" + id + "' already exists.");
      throw e;      
    }
    IdGenerator.addId(id);
    this.mId = id;

  }

  private static String generateId() {
    return IdGenerator.generateId("SETEdge");
  }

  public SETNode getHead() {
    return this.mHead;
  }

  public SETNode getTail() {
    return this.mTail;
  }

  public SET getSET() {
    return this.mSET;
  }

  public void setSET(SET set) {
    this.mSET = set;
  }
  
  public void setTail(SETNode node) {
    this.mTail = node;
  }
  
  public void setHead(SETNode node) {
    this.mHead = node;
  }
  
  public String getId() {
    return this.mId;
  }
  public void setEdgeAnnotation(Boolean bool){}
  public Boolean getEdgeAnnotation(){
    return false;
  }
}

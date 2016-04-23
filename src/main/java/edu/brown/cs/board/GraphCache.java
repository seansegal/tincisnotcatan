package edu.brown.cs.board;

import java.util.HashMap;
import java.util.Map;

public abstract class GraphCache {

  // TODO: fix so that we can support multiple games.
  public final static Map<Intersection, CatanNode> nodes = new HashMap<Intersection, CatanNode>();
  public final static Map<Path, CatanEdge> edges = new HashMap<Path, CatanEdge>();
}

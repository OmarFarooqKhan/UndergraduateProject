package project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alan P. Sexton
 * Date: 20/06/13
 * Time: 23:36
 */

/**
 * The <code>Model</code> class manages the data of the application
 * <p>
 * Other than possibly a draw method, which draws a representation of the object
 * on a graphics context, and possibly a toString method, which generates a
 * <code>String</code> representation of the object, it should not know about
 * the user interface
 * </p>
 */
public class Model
{
	private List<_Relation>	rel	= new ArrayList<_Relation>();
	private List<_Entity> ents = new ArrayList<>();
	private List<_Connection> con = new ArrayList<>();
	private List<_Generalise> gen = new ArrayList<>();

	public Model()
	{
	}

	public List<_Relation> getRelations()
	{
		return rel;
	}
	
	public List<_Entity> getEnts()
	{
		return ents;
	}
	public List<_Connection> getConnections(){
		return con;
	}
	
	public void addGen(_Generalise gene) {
		gen.add(gene);
	}
	public void addEnt(_Entity ent)
	{
		ents.add(ent);
	}
	
	public void addCon(_Connection connection)
	{
		con.add(connection);
	}
	public _Connection createConnection(_Entity entity, _Relation relation)
	{
		_Connection connection = new _Connection(entity,relation);
		entity.addConnection(connection);
		relation.addConnection(connection);
		con.add(connection);
		return connection;
	}

	/**
	 * Adds a new <code>Rectangle</code> to the <code>Model</code>
	 *
	 * @param rect
	 *            the <code>Rectangle</code> to add to the <code>Model</code>
	 */
	public void addRel(_Relation rela)
	{
		rel.add(rela);
	}
	
	public void removeRelations()
	{
		rel.clear();
	}
	
	public void removeGens() {
		gen.clear();
	}
	
	public void removeEnts()
	{
		ents.clear();
	}
	
	public void removeCons() {
		con.clear();
	}
	
	public void deleteEnt(_Entity e)
	{
		if(ents.contains(e)) {
			ents.remove(e);
		}
	}
	
	public void deleteRelation(_Relation r)
	{
		if(rel.contains(r)) {
			rel.remove(r);
		}
	}
	
	public void deleteGen(_Generalise gene)
	{
		if(gen.contains(gene)) {
			gen.remove(gene);
		}
	}
	public void deleteSubset(_Generalise gen,_Subset sub) {
		gen.removeSubset(sub);
	}
	
	public void removeConnections() {
		for(_Connection connection : con) {
			connection.getRelation().removeConnections(connection);
			connection.getEntity().removeConnection(connection);
		}
		con.clear();
	}
	public void deleteConnection(_Connection connection)
	{
		if(con.contains(connection)) {
			connection.getRelation().removeConnections(connection);
			connection.getEntity().removeConnection(connection);
			con.remove(connection);
		}
	}
	
	public void deleteAttribute(_Entity entity,_Attribute attribute) 
	{
		entity.removeAttribute(attribute);
	}
	
	public void deleteAttribute(_Relation relation,_Attribute attribute) 
	{
		relation.removeAttribute(attribute);
	}

	public List<_Generalise> getGen() {
		return gen;
	}
	

	

}

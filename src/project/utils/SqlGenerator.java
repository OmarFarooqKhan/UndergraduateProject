package project.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import project.model.Model;
import project.model._Entity;
import project.model._Relation;

public class SqlGenerator {

	private Model model;
	private String exportedsql = "Exported_SQL";
	private ModelFilter mf;
	private OneToOne onetoone;
	private OneToMany onetomany;
	private ManyToMany manytomany;
	private Nary nary;
	private EntityEvaluator ev;
	
	 ArrayList<_Entity> weakEnts = new ArrayList<>();
	 ArrayList<_Entity> otherEnts = new ArrayList<>();
	 ArrayList<_Entity> entitysToIgnore = new ArrayList<>();
	 ArrayList<_Relation> relationsToIgnore = new ArrayList<>();
	 BufferedWriter writer;
	 FileWriter fw;

	
	public SqlGenerator(Model m) {
		this.model = m;
		mf = new ModelFilter(this.model);
		onetoone = new OneToOne(this.model,this);
		onetomany = new OneToMany(this.model, this);
		manytomany = new ManyToMany(this.model);
		nary = new Nary(this.model);
		ev = new EntityEvaluator(this.model,this);
	}

	public boolean genSql() {
		String text = "";
		String text1 = "";
		String text2 = "";
		String text3 = "";
		String text4 = "";
		String text5 = "";
		String finaltext = "";
		mf.entityChecker();
		mf.relationChecker();
		
		if(mf.anythingToFix() == true) {
			System.out.println("hi");
			return false;
		}
		
		text += ev.evaluateWeakEntitys(text);
		text1 += onetoone.oneToOneCreation(text1);
		text2 += onetomany.oneToManyCreation(text2);
		text3 += manytomany.manyToManyCreation(text3);
		text4 += nary.naryCreation(text4);
		text5 += ev.evaluateGeneralEntitys(text5);
		System.out.println(text5);
		System.out.println(text);


		finaltext += text + text1 + text2 +text3 + text4 + text5;
		create(finaltext);
		return true;

	}

	private File fileWithDirectoryAssurance(String directory, String filename) {
		File dir = new File(directory + "/" + filename);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private void create(String name) {
		try {
			try {
				final String dir = System.getProperty("user.dir");
				// System.out.println("current dir = " + dir);
				File file = fileWithDirectoryAssurance(dir, exportedsql);
				File actualFile = new File(file, "sql.txt");
				fw = new FileWriter(actualFile);
				writer = new BufferedWriter(fw);
			} catch (IOException e) {
				e.printStackTrace();
			}

			writer.write(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			writer.close();
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public List<Object> getThingsToFix() {
		// TODO Auto-generated method stub
		return mf.getThingsToFix();
	}

}

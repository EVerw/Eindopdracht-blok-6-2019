/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eindopdracht.blok6.pkg2019;

/**
 *
 * @author Erik
 */
public class Gene {
    private int geneTaxID;
    private int geneGeneID;
    private String geneSymbol;
    private String geneLocusTag;
    private String geneChromosome;
    private String geneMap_Location;
    private String geneSynonyms;

    public Gene(int taxID, int geneID, String symbol, String locusTag, String chromosome, String map_Location, String synonyms) {
        this.geneTaxID =taxID;
        this.geneGeneID = geneID;
        geneSymbol = symbol;
        geneLocusTag = locusTag;
        geneChromosome = chromosome;
        geneMap_Location = map_Location;
        geneSynonyms = synonyms;
    }
    
    public int getTaxID() {
        return geneTaxID;
    }
    
    public Integer getGeneID() {
        return geneGeneID;
    }
    
    public String getSymbol() {
        return geneSymbol;
    }
    
    public String getLocusTag() {
        return geneLocusTag;
    }
    
    public String getChromosome() {
        return geneChromosome;
    }
    
    public String getMap_Location() {
        return geneMap_Location;
    }
     
    public String getSynonyms() {
        return geneSynonyms;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eindopdracht.blok6.pkg2019;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.JFileChooser;

/**
 *
 * @author Erik
 */
public class GeneLogica {

    private static JFileChooser fileChooser;
    private static ArrayList<Gene> geneLijst = new ArrayList();
    private static File geneFile;
    private static ArrayList<Integer> gevondenPubmedIDS = new ArrayList();
    private static HashMap<Integer, ArrayList<Integer>> pubmedIDS_geneIDS = new HashMap<>();

    public static String openGeneBestand() {    //Opent het gen bestand. En retouneerd het path naar het bestand.
        int reply;
        fileChooser = new JFileChooser();
        reply = fileChooser.showOpenDialog(null);
        if (reply == JFileChooser.APPROVE_OPTION) {
            geneFile = fileChooser.getSelectedFile();
        }
        return geneFile.toString();
    }

    public static void leesGeneBestand(String pad) {   //leest het bestand uit en stuurt het regel voor regel naar Line_To_Object. 
        //Vervolgens bepaald het het aantal genen uit het bestand en laat dit zien
        geneLijst.clear();
        BufferedReader inFile;
        try {
            inFile = new BufferedReader(new FileReader(pad));
            inFile.readLine();
            String line;
            while ((line = inFile.readLine()) != null) {
                Line_To_Object(line);
            }
            GeneGUI.jLabel3.setText(Integer.toString(geneLijst.size()) + " Genes found");
            inFile.close();
        } catch (FileNotFoundException ex) {
            GeneGUI.jLabel3.setText("File not found");
        } catch (IOException ex) {
            GeneGUI.jLabel3.setText("IOException");
        }
    }

    private static void Line_To_Object(String line) {   //Maakt van elke regel uit het gen bestand een gene object aan
        int taxID;
        int geneID;
        String[] parts = line.split("\t", -1);
        taxID = Integer.parseInt(parts[0]);
        geneID = Integer.parseInt(parts[1]);
        Gene gene = new Gene(taxID, geneID, parts[2], parts[3], parts[6], parts[7], parts[4]);
        geneLijst.add(gene);
    }

    static Integer zoekGenID(String genNaam) {    //Zoekt of het opgegeven gen voorkomt in het eerdere bestand. Het eventueel gevonden genID wordt teruggeven
        Integer geneID = 0;
        for (Gene gene : geneLijst) {
            if ((gene.getSymbol()).equals(genNaam)) {
                geneID = gene.getGeneID();
                break;
            }
        }
        return geneID;
    }

    public static void invulChecker() { //Controleerd of de gen naam is ingevuld en of het gen bestand al geopend is.
        if (geneLijst.isEmpty()) {
            GeneGUI.jLabel6.setText("Open eerst een gen info bestand");
        } else {
            String genNaam = GeneGUI.jTextField2.getText();
            if (genNaam != null) {
                int genID = zoekGenID(genNaam);
                if (genID == 0) {
                    GeneGUI.jLabel6.setText("Niks gevonden");
                } else {
                    pubmedLijst(genID);
                }
            } else {
                GeneGUI.jLabel6.setText("Vul eerst iets in");
            }
        }
    }

    public static void pubmedLijst(Integer searchGeneID) {  //Loopt door het pubmed bestand heen. Zoekt naar het gen ID van het opgegeven gen en slaat de bijbehorende nummers op.
                                                            //Alle regels gaan door naar relatedGenes.
        BufferedReader inFile;
        try {
            String fileString = (geneFile.getParent()).toString() + "\\gene2pubmed";
            inFile = new BufferedReader(new FileReader(fileString));
            inFile.readLine();
            String line;
            while ((line = inFile.readLine()) != null) {
                String[] parts = line.split("\t");
                relatedGenes(parts);
                if (searchGeneID.equals(Integer.parseInt(parts[1]))) {
                    gevondenPubmedIDS.add(Integer.parseInt(parts[2]));
                }
            }
        } catch (FileNotFoundException ex) {
            GeneGUI.jLabel6.setText("Pubmed bestand niet gevonden");
        } catch (IOException ex) {
            Logger.getLogger(GeneLogica.class.getName()).log(Level.SEVERE, null, ex);
        }
        GeneGUI.jLabel6.setText(Integer.toString(gevondenPubmedIDS.size()) + " PMIDS found");
    }

    public static void relatedGenes(String[] parts) {   //Zet de pubmedids gekoppelt met alle gen ids in een hashmap
        ArrayList<Integer> list = pubmedIDS_geneIDS.get(Integer.parseInt(parts[2]));
        if (list == null) {
            list = new ArrayList<Integer>();
            list.add(Integer.parseInt(parts[1]));
            pubmedIDS_geneIDS.put(Integer.parseInt(parts[2]), list);
        }
        pubmedIDS_geneIDS.put(Integer.parseInt(parts[2]), list);
    }

    public static void showRelatedGenes(ArrayList<Gene> geneObjectList) {   //Print de gevonden gerelateerde genen in de textarea in de GUI. Maakt eerst de textarea leeg.
        GeneGUI.jTextArea1.setText(null);
        GeneGUI.jTextArea1.append("Gen ID" + "\t" + "Tax ID" + "\t" + "Symbol" + "\t" + "Chromosome" + "\t" + "Locus Tag" + "\t" + "Map Location" + "\t" + "Synonyms" + "\n");
        GeneGUI.jLabel9.setText(Integer.toString(geneObjectList.size()) + " Related genes found");
        for (Gene gene : geneObjectList) {
            Integer geneID = gene.getGeneID();
            Integer geneTaxID = gene.getTaxID();
            String geneSymbol = gene.getSymbol();
            String geneChromosome = gene.getChromosome();
            String geneSynonyms = gene.getSynonyms();
            String geneLocusTag = gene.getLocusTag();
            String geneMapLocation = gene.getMap_Location();
            GeneGUI.jTextArea1.append(geneID.toString() + "\t" + geneTaxID.toString() + "\t" + geneSymbol + "\t" + geneChromosome + "\t" + geneLocusTag + "\t" + geneMapLocation + "\t" + geneSynonyms + "\n");

        }
    }

    public static void sort() { //Sorteerd de gerelateerde genen.
        ArrayList<Gene> relatedGeneObjectList = getRelatedGenesList();
        if (GeneGUI.jRadioButton4.isSelected()) {
            Collections.sort(relatedGeneObjectList, new GeneIDComparator());
        } else if (GeneGUI.jRadioButton5.isSelected()) {
            Collections.sort(relatedGeneObjectList, new MapLocationComparator());
        } else if (GeneGUI.jRadioButton6.isSelected()) {
            Collections.sort(relatedGeneObjectList, new SymbolComparator());

        }
        showRelatedGenes(relatedGeneObjectList);
    }

    public static ArrayList getRelatedGenesList() { //Vindt de gerelateerde genen uit de eerder gemaakt hashmap
        Set<Integer> geneIDSet = getGeneIDSet();
        ArrayList<Gene> relatedGeneLijst = new ArrayList();
        GeneGUI.jTextArea1.append("Gen ID" + "\t" + "Tax ID" + "\t" + "Symbol" + "\t" + "Chromosome" + "\t" + "Locus Tag" + "\t" + "Map Location" + "\t" + "Synonyms" + "\n");
        GeneGUI.jLabel9.setText(Integer.toString(geneIDSet.size()) + " Related genes found");
        for (Integer geneID : geneIDSet) {
            for (Gene gene : geneLijst) {
                if (Objects.equals(gene.getGeneID(), geneID)) {
                    relatedGeneLijst.add(gene);
                }
            }
        }
        return relatedGeneLijst;
    }

    public static Set getGeneIDSet() {  //Haalt duplicates uit de lijst van pubmedIDs
        Set<Integer> geneSet = new HashSet();
        for (Integer gevondenPubmedID : gevondenPubmedIDS) {
            ArrayList<Integer> list = pubmedIDS_geneIDS.get(gevondenPubmedID);
            for (Integer geneID : list) {
                geneSet.add(geneID);
            }
        }
        return geneSet;
    }

    public static void writeFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(((geneFile.getParent()).toString() + "\\found_pubmedIDS.txt"), "UTF-8");
            for (Integer pubmedID : gevondenPubmedIDS) {
                writer.println(pubmedID);;
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GeneLogica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GeneLogica.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eindopdracht.blok6.pkg2019;

import java.util.Comparator;

/**
 *
 * @author Erik
 */
public class GeneIDComparator implements Comparator<Gene>{

    public int compare(Gene a, Gene b) 
    { 
        return (a.getGeneID()).compareTo(b.getGeneID()); 
    } 
    
}

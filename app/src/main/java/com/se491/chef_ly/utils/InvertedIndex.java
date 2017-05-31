package com.se491.chef_ly.utils;


import java.util.*;


public class InvertedIndex {

    public static Map<String, String> termDict = new HashMap<String, String>();



    public static Map<String, ArrayList> termPostings = new HashMap<String, ArrayList>();

    public static Map<Integer, String> documentMap = new HashMap<Integer, String>();

    public static int numDocs = 0;


    public static void mergeTermDictionaries(Map<String, String> docDictionary){

        int newDictLen = docDictionary.size();

        for (String word: docDictionary.keySet()){

            String masterValue = termDict.get(word);

            if (masterValue != null){

                masterValue = String.valueOf(Integer.parseInt(masterValue) + Integer.parseInt(docDictionary.get(word)));
                termDict.remove(word);
                termDict.put(word, masterValue);
            } else {

                termDict.put(word, docDictionary.get(word));
            }
        }
    }




    public static void addDocPostings(Map<String, ArrayList> docPostings, String docText){

        documentMap.put(numDocs, docText);

        for(String word: docPostings.keySet()){
            Tuple<String, ArrayList> postingTuple = new Tuple( numDocs, docPostings.get(word));

            ArrayList postingsList = termPostings.get(word);

            if (postingsList != null){

                termPostings.remove(word);
                postingsList.add(postingTuple);

                termPostings.put(word, postingsList);
            } else {
                ArrayList<Tuple> newPostingsList = new ArrayList<>();
                newPostingsList.add(postingTuple);

                termPostings.put(word, newPostingsList);
            }
        }
    numDocs++;
    }



    public static void indexPhrase(String phrase){
        Map<String, String> docDict = new HashMap<String, String>();
        Map<String, ArrayList> docPostings = new HashMap<String, ArrayList>();


        String[] phraseArray = phrase.replaceAll("\\p{P}","").split("\\s+");


        for(int x=0; x < phraseArray.length ; x++){
            //System.out.println(phraseArray[x]);
            String word = phraseArray[x].toLowerCase();
            String value = docDict.get(word);

            if (value == null){
                docDict.put(word, "1");

                ArrayList<Integer> postingList = new ArrayList<>();
                postingList.add(x);
                docPostings.put(word, postingList);

            } else {
                docDict.remove(word);
                docDict.put(word, String.valueOf(Integer.parseInt(value)+1));

                ArrayList post = docPostings.get(word);
                post.add(x);
                docPostings.remove(word);
                docPostings.put(word, post);
            }


        }

        mergeTermDictionaries(docDict);

        addDocPostings(docPostings, phrase);


    }


    // TODO - add createTermDocMatrix method

    public static ArrayList mergePostingLists(String phraseA, String phraseB){
        // merge algo
        Tuple<String, ArrayList> blankTuple = new Tuple(999, new ArrayList<>());

        ArrayList<Tuple<String, ArrayList>> p1List = termPostings.get(phraseA);

        ArrayList<Tuple<String, ArrayList>> p2List = termPostings.get(phraseB);

        if (p1List == null && p2List ==null){
            ArrayList<Tuple> dummy = new ArrayList<Tuple>();
            dummy.add(blankTuple);
            return dummy;
        }
        if (p1List == null){
            return p2List;
        } else if (p2List == null){
            return p1List;
        }

        p1List.add(blankTuple);
        p2List.add(blankTuple);


        Iterator p1 = p1List.iterator();
        Iterator p2 = p2List.iterator();

        ArrayList<Tuple<String, ArrayList>> answer = new ArrayList<>();


        Tuple<String, ArrayList> p1Tuple = Tuple.class.cast(p1.next());
        Tuple<String, ArrayList> p2Tuple = Tuple.class.cast(p2.next());

        while (p1.hasNext() && p2.hasNext()){

            if (p1Tuple.x == p2Tuple.x){
                answer.add(p1Tuple);
                p1Tuple = Tuple.class.cast(p1.next());
                p2Tuple = Tuple.class.cast(p2.next());

            } else {
                if (Integer.parseInt(p1Tuple.getX()) < Integer.parseInt(p2Tuple.getX())) {
                    p1Tuple = Tuple.class.cast(p1.next());
                } else {
                    p2Tuple = Tuple.class.cast(p2.next());

                }
            }
        }

        return answer;
    }


    public static ArrayList queryIndex(String query){
        String[] words = query.toLowerCase().replaceAll("\\p{P}","").split(" ");

        ArrayList<Tuple> merged = mergePostingLists(words[0], words[1]);

        //start words at 2 as we use the first 2 above
        for (int t =2; t<words.length; t++){
            //TODO - acutally merge lists not just add after joing next words
            merged.addAll(mergePostingLists(words[t-1], words[t]));
        }

        Tuple<String, ArrayList> blankTuple = new Tuple(999, new ArrayList<>());

        Set<Tuple> dedupedPostingSet = new LinkedHashSet<>(merged);

        ArrayList<Tuple> dedupedPostingsList = new ArrayList<>(dedupedPostingSet);

        for(int t=0; t<dedupedPostingsList.size(); t++){
            if(dedupedPostingsList.get(t).getXAsInt() == 999){
                dedupedPostingsList.remove(t);
            }

        }


        return dedupedPostingsList;
    }


    public static void loadIngredientsInToIndex(List ingredients){

    }

    public static void loadDirectionsInToIndex(List directions){}


    public static void main(String[] args) {
	// write your code here


        String ingredients = "10 loose cups baby arugula\n" +
                "1/2 tsp Old Bay\n" +
                "2 1/2 tbsp champagne vinegar\n" +
                "3/4 cup cooked quinoa\n" +
                "1 1/ 4 tsp dijon mustard\n" +
                "2 tbsp Dijon mustard\n" +
                "1 large egg, beaten\n" +
                "1 large pink grapefruit, peeled and diced\n" +
                "1 cup kale, chopped\n" +
                "Kosher salt and freshly ground black pepper, to taste\n"+
                "some olive oil";


        for (String line: ingredients.split("\\n")){
            //System.out.println(line);
            indexPhrase(line);
        }

        String directions = "In a small bowl, whisk the olive oil, vinegar, shallots, dijon, salt and pepper.\n" +
                "Cut about a 4 oz piece off or the salmon and place in a food processor or chopper to finely chop. This will help hold the burgers together. With a knife finely chop the remaining salmon, transfer to a large work bowl.\n" +
                "Heat a large nonstick skillet over medium heat, add the oil and saute shallots and kale. Season with salt and pepper and cook over medium heat until wilted and tender, about 4 to 5 minutes.\n" +
                "Transfer to the bowl with salmon along with quinoa, Dijon, Old Bay and egg.";



        for (String direction: directions.split("\\n")){
            indexPhrase(direction);
        }


        String query = "How Much OliVe oIl?";

        System.out.println(query);

        ArrayList<Tuple> merged = queryIndex(query);


        for (int i =0; i<merged.size(); i++){
            System.out.println(documentMap.get(merged.get(i).getXAsInt()));
        }

    }
}

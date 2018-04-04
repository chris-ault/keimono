package keimono;import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CoreNlpExample {
    //public static final String[] nounverb = {NN NNS NNP NNPS VB VBD VBG VBP VBZ};

    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "This is some text. Hopefully the processor will read this fine. I have to pee.";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                //String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                //if(Arrays.asList(nounverb).contains(pos));
                if(pos=="NN"||pos=="NNS"||pos=="NNP"||pos=="NNPS"||pos=="VB"||pos=="VBD"||pos=="VBG"||pos=="VBP"||pos=="VBZ")
                System.out.println(String.format("Print: word: [%s] pos: [%s]", word, pos));
            }
        }
    }
}
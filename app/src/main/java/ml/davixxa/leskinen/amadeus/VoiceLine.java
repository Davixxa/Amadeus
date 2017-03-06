package ml.davixxa.leskinen.amadeus;

/**
 * Created by w890i on 06-03-2017.
 */

public class VoiceLine {

    final private int id;
    final private int mood;
    //final private int subtitle;

    VoiceLine(int id, int mood) {

        this.id = id;
        this.mood = mood;


    }

    int getId() {

        return id;

    }

    int getMood() {

        return mood;

    }

    int getSubtitle() {

        //return subtitle;
        return 0;

    }

}

package com.cse110.ucsd.flashbackmusicproject.utility;

import java.util.Random;

/**
 * Created by sseung385 on 3/2/18.
 */

public class NameGenerator {
    final int size = 20;
    Random random;
    final String[] nouns = { "Boy", "Girl", "Doctor", "Snake", "Magician", "Achiever", "Politician",
                             "Policeman", "Teacher", "Student", "Children", "Mate", "Buddy", "Lady",
            "Gentleman", "Boss", "Girlfriend", "Boyfriend", "Brother", "Assistant" };
    final String[] adjectives = { "Happy", "Thoughtful", "Famous", "Envious", "Excited", "Silly",
            "Harmonious", "Rude", "Abrasive", "Interesting", "Innocent", "Scary", "Magical",
            "Silent", "Careful", "Giant", "Righteous", "Great", "Underrated", "Overrated" };

    public NameGenerator() {
        random = new Random();
    }
    public String getName() {
        String adj = adjectives[ random.nextInt( size) ];
        String noun = nouns[ random.nextInt( size) ];
        int num = random.nextInt( size ) + 1;

        return ( adj + noun + num);
    }

}

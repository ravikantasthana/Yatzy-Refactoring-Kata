import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.groupingBy;

class DiceRolls implements Iterable<Integer> {
    private final int[] diceRolls;

    public DiceRolls(int d1, int d2, int d3, int d4, int d5) {
        this.diceRolls = new int[]{d1, d2, d3, d4, d5};
        if (streams().anyMatch(n -> n <=0 || n >=7 )) {
            throw new IllegalArgumentException("Dice roll should be between 1 and 6");
        }
    }

    public int sumValuesWithDiceRoll(int diceRoll) {
        return streams()
            .filter(i -> i == diceRoll)
            .mapToInt(Integer::intValue)
            .sum();
    }

    @Override
    public Iterator<Integer> iterator() {
        return streams().iterator();
    }

    public Stream<Integer> streams() {
        return IntStream.of(diceRolls).boxed();
    }

}

public class Yatzy {


    private static int i;

    public static int chance(DiceRolls diceRolls) {
        return diceRolls.streams().mapToInt(Integer::intValue).sum();
    }

    public static int yatzy(DiceRolls diceRolls) {
        //if (diceRolls.streams().allMatch(n -> n == diceRolls.iterator().next())){
//        if (diceRolls.streams().collect(toSet()).size() == 1){
        if (diceRolls.streams().distinct().count() == 1){
            return 50;
        }
        return 0;
    }

    public static int ones(DiceRolls diceRolls) {
        return diceRolls.sumValuesWithDiceRoll(1);
    }

    public static int twos(DiceRolls diceRolls) {
        return diceRolls.sumValuesWithDiceRoll(2);
    }

    public static int threes(DiceRolls diceRolls) {
        return diceRolls.sumValuesWithDiceRoll(3);
    }

    public static int fours(DiceRolls diceRolls) {
        return diceRolls.sumValuesWithDiceRoll(4);
    }

    public static int fives(DiceRolls diceRolls) {
        return diceRolls.sumValuesWithDiceRoll(5);
    }

    public static int sixes(DiceRolls diceRolls) {
        return diceRolls.sumValuesWithDiceRoll(6);
    }
    protected int[] dice;

    public Yatzy(int d1, int d2, int d3, int d4, int _5) {
        dice = new int[5];
        dice[0] = d1;
        dice[1] = d2;
        dice[2] = d3;
        dice[3] = d4;
        dice[4] = _5;
    }

    public static int score_pair(DiceRolls diceRolls) {

        return diceRolls.streams()
                .collect(groupingBy(Integer::intValue))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() >= 2)
                .max(Map.Entry.comparingByKey())
                .stream()
                .flatMap(entry -> entry.getValue().stream())
                .mapToInt(Integer::intValue)
                .limit(2)
                .sum();

    }

    public static int two_pair(DiceRolls diceRolls) {

        List<Integer> twoPairs = diceRolls.streams()
            .collect(groupingBy(Integer::intValue))
            .entrySet()
            .stream()
            .filter(e -> e.getValue().size() >= 2)
            .map(Map.Entry::getKey)
            .collect(toList());

        if(twoPairs.size() != 2){
            return 0;
        } else {
            return twoPairs.stream().mapToInt(Integer::intValue).sum() * 2;
        }

        /*List<Map.Entry<Integer, List<Integer>>> entries = diceRolls.streams()
            .collect(Collectors.groupingBy(Integer::intValue))
            .entrySet()
            .stream()
            .filter(entry -> entry.getValue().size() >= 2)
            .collect(Collectors.toList());

        if(entries.size() != 2){
            return 0;
        } else {
            return entries.stream()
                .flatMap(e -> e.getValue().subList(0, 2).stream())
                .mapToInt(Integer::intValue)
                .sum();
        }*/
    }

    public static int three_of_a_kind(DiceRolls diceRolls) {
        return nOfAKind(diceRolls, 3);
    }

    public static int four_of_a_kind(DiceRolls diceRolls) {
        return nOfAKind(diceRolls, 4);
    }

    private static int nOfAKind(DiceRolls diceRolls, int n) {
        return diceRolls.streams()
            .collect(groupingBy(Integer::intValue))
            .entrySet().stream()
            .filter(e -> e.getValue().size() >= n)
            .map(Map.Entry::getKey)
            .mapToInt(Integer::intValue).findFirst().orElse(0) * n;
    }


    public static int smallStraight(DiceRolls diceRolls) {

        Set<Integer> diceSet = diceRolls.streams().collect(toSet());
        boolean smallStraightMatch = IntStream.rangeClosed(1, 5).allMatch(n -> diceSet.contains(n));
        return smallStraightMatch ? 15 : 0;
    }

    public static int largeStraight(DiceRolls diceRolls) {
        Set<Integer> diceSet = diceRolls.streams().collect(toSet());
        boolean largeStraightMatch = IntStream.rangeClosed(2, 6).allMatch(n -> diceSet.contains(n));
        return largeStraightMatch ? 20 : 0;
    }

    public static int fullHouse(DiceRolls diceRolls) {

        Map<Integer, Long> fullHouseCounting = diceRolls.streams()
            .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));

        int fullHouseCount = 0;
        if(fullHouseCounting.size() == 2){
            for (Map.Entry<Integer, Long> fhcEntry : fullHouseCounting.entrySet()) {
                fullHouseCount += fhcEntry.getKey() * fhcEntry.getValue();
            }

        }

        return fullHouseCount;
    }
}




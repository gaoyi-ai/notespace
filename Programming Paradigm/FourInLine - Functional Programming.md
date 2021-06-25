---
title: FourInLine
categories:
- Functional Paradigm
tags:
- Java Functional Paradigm
date: 2019/8/1
---



@[toc]

# FourInLine 介绍

简单介绍：a  two-player  connection  game  in  which  the  players  first  choose  a  color  and  then  take  turns  dropping  colored  discs  into  a  seven-column,  six-row, vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The object of the game is to connect four of one's own discs of the same color next to each other  vertically, horizontally, or diagonally before your opponent. Four in a Line is a strongly solved game. The  first player  can  always  win by playing the right moves. 

[在线游戏](https://www.mathsisfun.com/games/connect4.html)

![在这里插入图片描述](https://gitee.com/gaoyi-ai/image-bed/raw/master/images/20200705191526514.png)

# 游戏逻辑

```java
public class FourInLine {

    // Declare some constants

    static int NColumns = 7;
    static int NRows = 6;

    // A player is either the red player, or the blue player

    public static Player redPlayer = Player.redPlayer;
    public static Player bluePlayer = Player.bluePlayer;


    // A piece is either a red piece or a blue piece

    public static Piece redPiece = Piece.redPiece;
    public static Piece bluePiece = Piece.bluePiece;


    // A column is a list of Pieces.  The first element of the list represents the top of
    // the column, e.g.
    // row 6 --
    // row 5 --
    // row 4 -- RedPiece   <- first element of the list
    // row 3 -- RedPiece
    // row 2 -- BluePiece
    // row 1 -- RedPiece   <- last element in the list
    // The list for this column would be [redPiece, redPiece, bluePiece, redPiece]
    // Now, to add a piece to the TOP of a column, just create a new column
    // with that piece and append the rest of the old column to it

    // a Column is a list of pieces

    public static class Column extends ArrayList<Piece> {
        public Column() {
        }
        public Column(List<Piece> l) {
            this.addAll(l);
        }
    }

    // The GameState is a list of Columns

     public static class GameState extends ArrayList<Column> {
        public GameState() {
        }
        public GameState(List<List<Piece>> g) {
            List<Column> c = g.stream().map(Column::new).collect(toList());
            this.addAll(c);
        }
     }


    // ColumnNums are 1-based, but list indices are 0-based.  indexOfColumn converts
    // a ColumnNum to a list index.

    public static class ColumnNum {
        int index;
        public ColumnNum(int index) {
            GameState s;
            this.index = index;
        }
        public int indexOfColumn() {
            return index - 1;
        }
        public String toString() {
            return "" + index;
        }
    }

    //
    //   Convert a column to a string of the form "rBrrBB", or "   rrB".  The string
    //   must have length 6.  If the column is not full, then the list should be
    //   prefixed with an appropriate number of spaces
    //

    //   Convert a column to a string of the form "rBrrBB", or "   rrB".  The string
    //   must have length 6.  If the column is not full, then the list should be
    //   prefixed with an appropriate number of spaces

    public static String showColumn(Column xs) {
        List<String> blanks = Collections.nCopies( 6 - xs.size(), " ");
        return String.join("", blanks) +
                xs.stream().map(Piece::toString).collect(joining(""));
    }


    //
    //  Convert a GameState value to a string of the form:
    //  "    r        \n
    //   r   r   B   r\n
    //   B B r   B r B\n
    //   r B r r B r r\n
    //   r B B r B B r\n
    //   r B r r B r B"
    //   Useful functions:
    //     showColumn
    //       (which you already defined)
    //     and transposes a list of lists using streams,
    //       so List(List(1,2,3), List(4,5,6)) becomes List(List(1,4), List(2,5), List(3,6))

    public static String showGameState(GameState xs) {
        return IntStream.range(0, NRows)
                .mapToObj(i -> xs.stream().map(l -> showColumn(l))
                        .map(l -> l.charAt(i) + "")
                        .collect(joining(" ")))
                .collect(joining("\n"));
    }

    // Which pieces belong to which players?

    public static Piece pieceOf(Player player)  {
        if (player instanceof Player.RedPlayer)
            return redPiece;
        else
            return bluePiece;
    }

    // Given a player, who is the opposing player?

    public static Player otherPlayer(Player player) {
        if (player instanceof Player.RedPlayer)
            return Player.bluePlayer;
        else
            return Player.redPlayer;
    }


    // Given a piece, what is the colour of the other player's pieces?

    public static Piece otherPiece(Piece piece) {
        if (piece instanceof Piece.RedPiece)
            return bluePiece;
        else
            return redPiece;
    }


    // The initial GameState, all columns are empty.  Make sure to create the proper
    // number of columns

    public static GameState initGameState() {
        GameState game =  new GameState();
        game.addAll(Collections.nCopies(7, new Column()));
        return game;
    }


    // Check if a column number is valid (i.e. in range)

    public static boolean isValidColumn(ColumnNum c) {
        return c.index >= 1 && c.index <= NColumns;
    }


    // Check if a column is full (a column can hold at most nRows of pieces)

    public static boolean isColumnFull(Column column) {
        return column.size() >= NRows;
    }


    // Return a list of all the columns which are not full (used by the AI)

    public static List<ColumnNum> allViableColumns(GameState game) {
        return game.stream()
                .filter(c -> !isColumnFull(c))
                .map(c -> new ColumnNum(game.indexOf(c) + 1))
                .collect(toList());
        // another version
        // return IntStream.range(0, game.size())
        //         .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, game.get(i).size()))
        //         .collect(toList()).stream()
        //         .filter(i -> i.getValue() < 6)
        //         .map(i -> new ColumnNum(i.getKey() + 1))
        //         .collect(toList());
    }

    // Check if the player is able to drop a piece into a column

    public static boolean canDropPiece(GameState game, ColumnNum columnN) {
        return !isColumnFull(game.get(columnN.indexOfColumn()));
    }

    // Drop a piece into a numbered column, resulting in a new gamestate

    public static GameState dropPiece(GameState game, ColumnNum columnN, Piece piece) {
        GameState g = new GameState();
        Column c = new Column();
        c.add(piece);
        c.addAll(game.get(columnN.indexOfColumn()));
        g.addAll(game);
        g.set(columnN.indexOfColumn(), c);
        return g;
    }

    // Are there four pieces of the same colour in a column?

    static boolean fourInCol(Piece piece, Column col) {
    	// 当有四个棋子处于同一列时，游戏就已经结束了，所以只检验每一列从上向下前四个或者前1到5个(对方再下一个)
        return IntStream.range(0, 2).anyMatch( i -> col.size() > (i+3) && col.subList(i, i + 4).stream().allMatch(piece::equals));
        // another version 对每列连续同色棋子求和，遇到不同色就把count置0，同色就加1，累加到4保持（防止再次置0）
        // int reduce = col.stream()
        //         .mapToInt((p) -> p.equals(piece) ? 1 : 0)
        //         .reduce(0, (c, p) -> c >= 4 ? c : (p == 1 ? c + 1 : 0));
        // return reduce >= 4;
    }

    public static boolean fourInColumn(Piece piece, GameState game) {
        return game.stream().anyMatch(c -> fourInCol(piece, c));
    }


    // transposes gameboard, assumes all columns are full
    static GameState transpose(GameState g) {
        return new GameState(IntStream.range(0, g.get(0).size())
                .mapToObj(i -> g.stream()
                        .map(l -> l.get(i))
                        .collect(toList()))
                .collect(toList()));
    }
    // A helper function that fills up a column with pieces of a certain colour.  It
    // is used to fill up the columns with pieces of the colour that
    // fourInRow/fourInDiagonal is not looking for.  This will make those functions
    // easier to define.

    static Column fillBlank(Piece piece, Column column) {
        Column result = new Column(Collections.nCopies(NRows - column.size(), piece));
        result.addAll(column);
        return result;
    }

    // Are there four pieces of the same colour in a row?  Hint: use fillBlanks and
    // transpose to reduce the problem to fourInColumn

    public static boolean fourInRow(Piece piece, GameState game) {
        GameState transposed = transpose(new GameState(game.stream()
                .map(c -> fillBlank(otherPiece(piece), c))
                .collect(toList())));
        return fourInColumn(piece, transposed);
    }


    // Another helper function for fourInDiagonal.  Remove n pieces from the top of
    // a full column and add blanks (of the colour we're not looking for) to the
    // bottom to make up the difference.  This makes fourDiagonal easier to define.

    static Column shift(int n, Piece piece, Column column) {
    	// 从第n个截取，然后在后面加上n个
        Column c = new Column(column.subList(n, column.size()));
        c.addAll(Collections.nCopies(n, piece));
        return c;
    }

    // Are there four pieces of the same colour diagonally?  Hint: define a helper
    // function using structural recursion over the gamestate, and using shift and fourInRow.

    static boolean fourDiagonalHelper(GameState g, Piece piece) {
        Piece op = otherPiece(piece);
        if (g.size() < 4)
            return false;
       // 分别上移0，1，2，3格
       GameState ng = new GameState(Stream.of(g.get(0), shift(1, op, g.get(1)), shift(2, op, g.get(2)), shift(3, op, g.get(3))).collect(toList()));
       GameState next = new GameState((List)g);
       // 再向右检查
       next.remove(0);
        if (fourInRow(piece, ng))
            return true;
        else
            return fourDiagonalHelper(next, piece);
    }

    public static boolean fourDiagonal(Piece piece, GameState game) {
        Piece op = otherPiece(piece);
        GameState fullCS = new GameState(game.stream().map(c -> fillBlank(op, c)).collect(toList()));
        GameState revCS = new GameState();
        revCS.addAll(fullCS);
        Collections.reverse(revCS);
        // 左上->右下 || 右上->左下
        return fourDiagonalHelper(fullCS, piece) || fourDiagonalHelper(revCS, piece);
    }

    // Are there four pieces of the same colour in a line (in any direction)

    public static boolean fourInALine(Piece piece, GameState game) {
        return  fourDiagonal(piece, game) || fourInRow(piece, game) || fourInColumn(piece, game);
    }

    // Who won the game.  Returns an Optional since it could be that no one has won the
    // game yet.

    public static Optional<Player> winner(GameState game) {
        if (fourInALine(redPiece, game))
            return Optional.of(redPlayer);
        else if (fourInALine(bluePiece, game))
            return Optional.of(bluePlayer);
        else
            return Optional.empty();
    }

}
```

# 游戏相关

```java
public class Game  {

  public static void main(String[] args) {
    // start game loop
    startGame();
  }


  // A map (similar to a dictionary in Python) that maps
  // Players to the functions that get moves for those players.  This will
  // allow us to use the same code for human vs. human matches as for computer
  // vs. human and computer vs. computer.

    static class MoveGetterMap extends HashMap<Player, Function<GameState, ColumnNum>> {}

  // How many moves should the AI look ahead.  Higher numbers mean a smarter AI,
  // but it takes much longer to evaluate the game tree.

  static int aiDepth = 4;
  static ColumnNum lastMove;

  // UI routines

  static void startGame() {

    System.out.println("Welcome to four-in-line");

    Function<GameState, ColumnNum> redPlayer = getMoveGetter(FourInLine.redPlayer);
    Function<GameState, ColumnNum> bluePlayer = getMoveGetter(FourInLine.bluePlayer);

    MoveGetterMap moveGetter = new MoveGetterMap();
    moveGetter.put(FourInLine.redPlayer, redPlayer);
    moveGetter.put(FourInLine.bluePlayer, bluePlayer);
    drawBoard(initGameState());
    turn(moveGetter, FourInLine.redPlayer, initGameState());

  }

  // Execute a single turn

  static void turn(MoveGetterMap moveGetter, Player player, GameState game) {

    Optional<Player> win = winner(game);

    if (win.isPresent()) {
        drawBoard(game);
        System.out.printf("%s wins!%n", win.get().toString());
    } else if (allViableColumns(game).isEmpty()) {
        drawBoard(game);
        System.out.println("It's a draw!");
    } else {
        ColumnNum c = (moveGetter.get(player)).apply(game);
        lastMove = c;
        GameState gameP = dropPiece(game, c, pieceOf(player));
        drawBoard(gameP);
        turn(moveGetter, otherPlayer(player), gameP);
    }
  }

  // gets a function that gets the next move for a particular player.
  // Depending on whether the player is human or computer, it will be
  // getHumanMove player, or getComputerMove player

  static  Function<GameState, ColumnNum> getMoveGetter(Player player) {

    System.out.printf("Is %s to be human or computer? ", player);

    Scanner scanner = new Scanner(System.in);
    String ln = scanner.nextLine().trim();

    if (ln.equals("computer")) {
      return aiMove(aiDepth, player);
    } else if (ln.equals("human")) {
      return getHumanMove(player);
    } else {
      System.out.println("Input must be either \"human\" or \"computer\"");
      return getMoveGetter(player);
    }
  }


  static ColumnNum getValidMove(GameState game) {
      ColumnNum c = getMove();
      if (!canDropPiece(game, c)) {
          System.out.printf("Column %s is full, try again.%n", c);
          return getValidMove(game);
      } else
          return c;
  }

  static ColumnNum getMove() {
        Scanner scanner = new Scanner(System.in);
        String ln = scanner.nextLine().trim();
        Optional<ColumnNum> c = getColumn(ln);
        if (c.isPresent()) {
            if (!isValidColumn(c.get()))  {
                System.out.println("No such column, try again.");
                System.out.println("Enter column number: ");
                getMove();
            }
        } else {
            System.out.println("That wasn't a number. Enter column number: ");
            getMove();
        }
        return c.get();
    }

  // Read a valid move

  static Function<GameState, ColumnNum> getHumanMove(Player player)  {
      return game -> {
          System.out.printf("%s's turn. Enter column number: ", player);
          return getValidMove(game);
      };
  }

  // Parse a column number from a string

  static Optional<ColumnNum> getColumn(String str) {
    String c = str.trim();
    try {
        return Optional.of(new ColumnNum(Integer.parseInt(c)));
    } catch (Exception e){
        return Optional.empty();
    }
  }

  // Draw the game board

  static void drawBoard(GameState gameState) {

    String[] strLines = showGameState(gameState).split("\n");

    List<Integer> c = Arrays.asList(6, 5, 4, 3, 2, 1);
    System.out.print(" ");
    for(int i = 1; i <= 7; i++) {
        if (lastMove != null && lastMove.index == i)
            System.out.printf("%s*", i); // highlight last played column
        else
            System.out.printf("%s ", i);
    }
    System.out.println("");

    for(Integer i: c) {
        System.out.printf("%s %s%n", i, strLines[6 - i]);
    }
  }
}
```

# minmax 算法

```java
public class GameTree {
    static class Tree {
         GameState game;
         List<Move> moves;
         public Tree(GameState g, List<Move> m) {
             game = g;
             moves = m;
         }
    }
    static class Move {
        ColumnNum move;
        Tree tree;
        public Move(ColumnNum m, Tree t) {
            move = m;
            tree = t;
        }
    }

    static Move subGameTree(GameState game, ColumnNum c, Player player, int depth) {
        return new Move(c, gameTree(otherPlayer(player),depth - 1, dropPiece(game, c, pieceOf(player))));
    }

    // Recursively build the game tree using allViableColumns to get all possible
    // moves (introduce depth as the function is not lazy).  Note that the tree bottoms out once the game is won

    static Tree gameTree(Player player, int depth, GameState game)  {

        Optional<Player> w = winner(game);
        if (w.isPresent()) {
            return new Tree(game, new ArrayList<>());
        } else if (depth == 0) {
            return new Tree(game, new ArrayList<>());
        } else {
            List<Move> moves = allViableColumns(game).stream().map(n -> {
                return subGameTree(game, n, player, depth);
            }).collect(toList());
            return new Tree(game, moves);

        }

    }

    //Estimate the value of a position for a player. This implementation only
    //assigns scores based on whether or not the player has won the game.  This is
    //the simplest possible way of doing it, but it results in an
    //overly-pessimistic AI.
    //
    //The "cleverness" of the AI is determined by the sophistication of the
    //estimate function.
    //Some ideas for making the AI smarter:
    //1) A win on the next turn should be worth more than a win multiple turns
    //later.  Conversely, a loss on the next turn is worse than a loss several
    //turns later.
    //2) Some columns have more strategic value than others.  For example, placing
    //pieces in the centre columns gives you more options.
    //3) It's a good idea to clump your pieces together so there are more ways you
    //could make four in a line.

    static int estimate(Player player, GameState game) {
        if (fourInALine(pieceOf(player), game))
            return 100;
        else if (fourInALine(pieceOf(otherPlayer(player)), game))
            return -100;
        else
            return 0;
    }

    static ColumnNum maxmini(Player player, Tree tree)  {
        if (tree.moves.isEmpty())
            throw new RuntimeException("The AI was asked to make a move, but there are no moves possible.  This cannot happen");
        else {

            return  tree.moves.stream()
                    .collect(Collectors.maxBy((Move a, Move b) -> {
                        return minimaxP(player, a.tree) - minimaxP(player, b.tree);
                    })).get().move;
        }

    }


    // Maximise the minimum utility of player making a move.  Do this when it is the
    // player's turn to find the least-bad move, assuming the opponent will play
    // perfectly.

    static int maxminiP(Player player, Tree tree) {
        if (tree.moves.isEmpty())
            return estimate(player, tree.game);
        else {
            return Collections.max(tree.moves.stream().map(m -> minimaxP(player, m.tree)).collect(toList()));
        }
    }

    // Minimise the maximum utility of player making a move.  Do this when it is the
    // opponent's turn, to simulate the opponent choosing the move that results in
    // the least utility for the player.

    static int minimaxP(Player player, Tree tree) {
        if (tree.moves.isEmpty())
            return estimate(player, tree.game);
        else {
            return Collections.min(tree.moves.stream().map(m -> maxminiP(player, m.tree)).collect(toList()));
        }
    }

    // Determine the best move for computer player

    public static Function<GameState, ColumnNum> aiMove(int lookahead, Player player) {
        return x -> {
            return maxmini(player,gameTree(player, lookahead, x));
        };
    }

}
```

# 游戏其他

```java
public abstract class Piece {
    private String name;
    public static RedPiece redPiece = new RedPiece();
    public static BluePiece bluePiece = new BluePiece();
    public String toString() {
        return name;
    }
    public Piece(String name) {
        this.name = name;
    }

    public static final class RedPiece extends Piece {

        private RedPiece() {
            super("r");
        }
    }

    public static final class BluePiece extends Piece {

        private BluePiece() {
            super("B");
        }
    }
}

public abstract class Player {
    private String name;
    public static RedPlayer redPlayer = new RedPlayer();
    public static BluePlayer bluePlayer = new BluePlayer();
    public String toString() {
        return name;
    }
    public Player(String name) {
        this.name = name;
    }

    public static final class RedPlayer extends Player {

        private RedPlayer() {
            super("Red Player");
        }
    }

    public static final class BluePlayer extends Player {

        private BluePlayer() {
            super("Blue Player");
        }
    }

}
```


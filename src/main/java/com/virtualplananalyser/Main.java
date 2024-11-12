package com.company;

import java.util.*;

class QueryNode {
    String aspQuery;
    int aspFrequency;
    int aspHeight;
    QueryNode aspLeft, aspRight;

    public QueryNode(String aspQuery) {
        this.aspQuery = aspQuery;
        this.aspFrequency = 1;
        this.aspHeight = 1;
    }
}

class AVLSearchTree {
    private QueryNode aspRoot;
    private final Map<String, Integer> aspSearchLog = new HashMap<>();  // For tracking top searches

    private int aspHeight(QueryNode aspNode) {
        return aspNode == null ? 0 : aspNode.aspHeight;
    }

    private void aspUpdateHeight(QueryNode aspNode) {
        aspNode.aspHeight = 1 + Math.max(aspHeight(aspNode.aspLeft), aspHeight(aspNode.aspRight));
    }

    private QueryNode aspRightRotate(QueryNode aspY) {
        QueryNode aspX = aspY.aspLeft;
        QueryNode aspT2 = aspX.aspRight;

        aspX.aspRight = aspY;
        aspY.aspLeft = aspT2;

        aspUpdateHeight(aspY);
        aspUpdateHeight(aspX);

        return aspX;
    }

    private QueryNode aspLeftRotate(QueryNode aspX) {
        QueryNode aspY = aspX.aspRight;
        QueryNode aspT2 = aspY.aspLeft;

        aspY.aspLeft = aspX;
        aspX.aspRight = aspT2;

        aspUpdateHeight(aspX);
        aspUpdateHeight(aspY);

        return aspY;
    }

    private int aspGetBalance(QueryNode aspNode) {
        return aspNode == null ? 0 : aspHeight(aspNode.aspLeft) - aspHeight(aspNode.aspRight);
    }

    public void aspInsertQuery(String aspQuery) {
        if (aspQuery == null || aspQuery.trim().isEmpty()) {
            System.out.println("Invalid query. Please provide non-empty queries only.");
            return;
        }
        aspRoot = aspInsertQuery(aspRoot, aspQuery.toLowerCase());
    }

    private QueryNode aspInsertQuery(QueryNode aspNode, String aspQuery) {
        if (aspNode == null) {
            return new QueryNode(aspQuery);
        }

        if (aspQuery.equals(aspNode.aspQuery)) {
            aspNode.aspFrequency++;  // Word already exists; increment frequency
            return aspNode;
        } else if (aspQuery.compareTo(aspNode.aspQuery) < 0) {
            aspNode.aspLeft = aspInsertQuery(aspNode.aspLeft, aspQuery);
        } else {
            aspNode.aspRight = aspInsertQuery(aspNode.aspRight, aspQuery);
        }

        aspUpdateHeight(aspNode);
        return aspBalance(aspNode, aspQuery);
    }

    private QueryNode aspBalance(QueryNode aspNode, String aspQuery) {
        int aspBalance = aspGetBalance(aspNode);

        if (aspBalance > 1 && aspQuery.compareTo(aspNode.aspLeft.aspQuery) < 0) {
            return aspRightRotate(aspNode);
        }

        if (aspBalance < -1 && aspQuery.compareTo(aspNode.aspRight.aspQuery) > 0) {
            return aspLeftRotate(aspNode);
        }

        if (aspBalance > 1 && aspQuery.compareTo(aspNode.aspLeft.aspQuery) > 0) {
            aspNode.aspLeft = aspLeftRotate(aspNode.aspLeft);
            return aspRightRotate(aspNode);
        }

        if (aspBalance < -1 && aspQuery.compareTo(aspNode.aspRight.aspQuery) < 0) {
            aspNode.aspRight = aspRightRotate(aspNode.aspRight);
            return aspLeftRotate(aspNode);
        }

        return aspNode;
    }

    public int aspSearchQueryFrequency(String aspQuery) {
        QueryNode aspResult = aspSearch(aspRoot, aspQuery.toLowerCase());
        if (aspResult != null) {
            aspResult.aspFrequency++;  // Increase frequency each time a query is searched
            aspSearchLog.put(aspQuery, aspResult.aspFrequency);  // Update search log
        }
        return aspResult == null ? 0 : aspResult.aspFrequency;
    }

    private QueryNode aspSearch(QueryNode aspNode, String aspQuery) {
        if (aspNode == null || aspNode.aspQuery.equals(aspQuery)) {
            return aspNode;
        }
        if (aspQuery.compareTo(aspNode.aspQuery) < 0) {
            return aspSearch(aspNode.aspLeft, aspQuery);
        } else {
            return aspSearch(aspNode.aspRight, aspQuery);
        }
    }

    public void aspDisplayQueries() {
        System.out.println("\nQuery frequencies (sorted):");
        aspDisplayQueries(aspRoot);
    }

    private void aspDisplayQueries(QueryNode aspNode) {
        if (aspNode != null) {
            aspDisplayQueries(aspNode.aspLeft);
            System.out.printf("%-15s : %d%n", aspNode.aspQuery, aspNode.aspFrequency);
            aspDisplayQueries(aspNode.aspRight);
        }
    }

    public void aspDisplayTopSearches(int aspN) {
        System.out.println("\nTop " + aspN + " search queries:");
        aspSearchLog.entrySet().stream()
                .sorted((aspA, aspB) -> aspB.getValue().compareTo(aspA.getValue())) // Sort by frequency in descending order
                .limit(aspN)
                .forEach(aspEntry -> System.out.printf("%-15s : %d%n", aspEntry.getKey(), aspEntry.getValue()));
    }
}

public class Main {
    public static void main(String[] args) {
        AVLSearchTree aspSearchTree = new AVLSearchTree();
        Scanner aspScanner = new Scanner(System.in);

        System.out.println("Enter search queries. Type 'exit' to finish entering queries.");

        while (true) {
            System.out.print("Enter query: ");
            String aspQuery = aspScanner.nextLine();

            if (aspQuery.equalsIgnoreCase("exit")) {
                break;
            }

            aspSearchTree.aspInsertQuery(aspQuery);
        }

        System.out.println("\nAll entered queries with frequencies:");
        aspSearchTree.aspDisplayQueries();

        System.out.println("\nEnter a query to search for its frequency. Type 'exit' to end.");
        while (true) {
            System.out.print("Query to search: ");
            String aspSearchQuery = aspScanner.nextLine();

            if (aspSearchQuery.equalsIgnoreCase("exit")) {
                break;
            }

            int aspFrequency = aspSearchTree.aspSearchQueryFrequency(aspSearchQuery);
            System.out.println("Frequency of '" + aspSearchQuery + "': " + aspFrequency);
        }

        System.out.println("\nEnter the number of top searches you want to display:");
        int aspTopN = aspScanner.nextInt();
        aspSearchTree.aspDisplayTopSearches(aspTopN);

        aspScanner.close();
    }
}

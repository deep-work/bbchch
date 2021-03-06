package ca.mcgill.softwarerebels;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class Graph
{
    public CommitNode rootNode;
    public ArrayList nodes=new ArrayList();

    public int[][] adjMatrix;//Edges will be represented as adjacency Matrix
    int size;
    public void setRootNode(CommitNode n)
    {
        this.rootNode=n;
    }

    public CommitNode getRootNode()
    {
        return this.rootNode;
    }

    public void addNode(CommitNode n)
    {
        nodes.add(n);
    }

    //This method will be called to make connect two nodes
    public void connectNode(CommitNode start,CommitNode end)
    {
        if(adjMatrix==null)
        {
            size=nodes.size();
            adjMatrix=new int[size][size];
        }

        int startIndex=nodes.indexOf(start);
        int endIndex=nodes.indexOf(end);
        adjMatrix[startIndex][endIndex]=1;
        //adjMatrix[endIndex][startIndex]=1;
    }

    private CommitNode getUnvisitedChildNode(CommitNode n)
    {

        int index=nodes.indexOf(n);
        int j=0;
        while(j<size)
        {
            if(adjMatrix[index][j]==1 && ((CommitNode)nodes.get(j)).visited==false)
            {
                return (CommitNode)nodes.get(j);
            }
            j++;
        }
        return null;
    }

    private int getImmediateChildrenCount(CommitNode n)
    {
        int count = 0;
        int index=nodes.indexOf(n);
        int j=0;
        while(j<size)
        {
            if(adjMatrix[index][j]==1 && ((CommitNode)nodes.get(j)).visited==false)
            {
                count++;
            }
            j++;
        }
        return count;
    }

    //BFS traversal of a tree is performed by the bfs() function
    public void bfs()
    {

        //BFS uses Queue data structure
        Queue q=new LinkedList();
        q.add(this.rootNode);
        printNode(this.rootNode);
        rootNode.visited=true;
        int kidCount = getImmediateChildrenCount(rootNode);
        if (kidCount < 2){
            System.out.printf("Not enough kids.\n");
            clearNodes();
            return;
        }
        while(!q.isEmpty())
        {
            CommitNode n=(CommitNode)q.remove();
            CommitNode child=null;
            printParentalInfo(n, kidCount);
            while((child=getUnvisitedChildNode(n))!=null)
            {
                child.visited=true;
                child.distanceToSolution = n.distanceToSolution+1;
                printNode(child);
                if(("passed").equals(child.status)){
                    System.out.printf("Found it! commit:%s kidcount:%s distance:%s\n", rootNode.label, kidCount, child.distanceToSolution);
                    clearNodes();
                    return;
                }
                q.add(child);
            }
        }
        //Clear visited property of nodes
        clearNodes();
    }

    //DFS traversal of a tree is performed by the dfs() function
    public void dfs()
    {
        //DFS uses Stack data structure
        Stack s=new Stack();
        s.push(this.rootNode);
        rootNode.visited=true;
        printNode(rootNode);
        while(!s.isEmpty())
        {
            CommitNode n=(CommitNode)s.peek();
            CommitNode child=getUnvisitedChildNode(n);
            if(child!=null)
            {
                child.visited=true;
                printNode(child);
                s.push(child);
            }
            else
            {
                s.pop();
            }
        }
        //Clear visited property of nodes
        clearNodes();
    }

    public ArrayList findPotentialStartPoints(){
        ArrayList potentials=new ArrayList();
        for (Object cnode : this.nodes) {
                CommitNode cn = (CommitNode) cnode;
//                System.out.println("checking "+cn.label);
                    if (cn.status.equals("passed")){
                        CommitNode child=null;
                        while((child=getUnvisitedChildNode(cn))!=null){
                            if (!child.status.equals("passed")){
                                potentials.add(child);
                            }
                            child.visited = true;
                        }
                        clearNodes();
                    }

            }
            return potentials;
    }

    int getLongestPathLength(CommitNode node) {
        if(node == null) return 0;
        if(node.status.equals("passed")) return 0;
        int max = 0;
        CommitNode child = null;
        while((child = getUnvisitedChildNode(node))!=null){
            max = Math.max(getLongestPathLength(child),max);
            child.visited = true;
        }
        return 1+max;
    }


    //Utility methods for clearing visited property of node
    private void clearNodes()
    {
        System.out.printf("To clean: %s\n",size);
        int i=0;
        while(i<size)
        {
            CommitNode n=(CommitNode)nodes.get(i);
            if (n==null){
                System.out.printf("Current node size: %s missing i: %s",size,i);
            } else {
                n.visited = false;
                n.distanceToSolution = 0;
            }
            i++;
        }
//        System.out.println("Cleaning done.");
    }

    //Utility methods for printing the node's label
    private void printNode(CommitNode n)
    {
        System.out.println("\n"+ n.label+" depth:"+n.distanceToSolution+" status:"+n.status);
    }

    private void printParentalInfo(CommitNode n, int c)
    {
        System.out.println("\n"+ n.label+" no .of kids:"+c);
    }





}


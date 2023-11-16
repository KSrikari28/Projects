#include<stdio.h>
#include<stdlib.h>
#include<string.h>
struct edge{
char *src,*dest;
struct edge *next;
};
typedef struct edge connector;
connector*head = NULL;
//to create singular node for adjacency list
struct node{
char *name;
};
//structure to create the adjacency list containing the node from above and a pointer to
//point to the next in the list
struct adj_list{
struct node NODE;
struct adj_list *following;
};
typedef struct adj_list adjDT;
//structure to create the complete graph containing the adjacency list with a pointer
//pointing to down to the next adjacency list of each node
struct graph_DT{
struct adj_list *adj;
struct graph_DT *down;
};
typedef struct graph_DT gDT;
struct graph_DT *start;


void display(connector*head);
void create_edge();
void create_graph(connector *head);
void display_graph(gDT *start);
void out_degree(gDT *start);
struct graph_DT * getcaught(gDT *start);


int main(){
create_edge();
display(head);
create_graph(head);
display_graph(start);
out_degree(start);
}

void create_edge(){
connector *cur=NULL;
FILE *ptr;
char data[100];
char src[10],dest[10];
int count;
ptr= fopen("data.txt","r");
if(ptr==NULL){
printf("no such file");
//return 0;
}
while(fscanf(ptr,"%s %s",src,dest)!= EOF)
{
if(strcmp(src,dest)==0){
printf("ignoring %c %c \n", *src,*dest);
continue;
}
connector *new = malloc(sizeof(connector));
new->src = strdup(src);
new->dest = strdup(dest);
new->next = NULL;
if(head==NULL)
{
cur = head=new;


}
else
{
cur=cur->next=new;
}
} // end of while loop to read file
fclose(ptr);
cur = NULL;
}


void display(connector*head){
connector *cur=NULL;
printf("displaying edges\n");
for(cur=head;cur;cur=cur->next)
{
printf("%s %s \n",cur->src,cur->dest);
}
free(cur);
}


void create_graph(connector *head){
struct graph_DT *mover=NULL,*caught,*transient=NULL;// pointer to move along
//the graph
connector *cur=NULL; // for the for loop of edges
struct adj_list *traveller,*a;
gDT *graph1 = malloc(sizeof(gDT));
gDT *graph;
int counter;
int Not_exists;
for(cur=head;cur;cur=cur->next) //traverse the edges and create adjacency list
{
if(start==NULL)//for first element of adjacency list
{
gDT *graph = malloc(sizeof(gDT));
adjDT *adjacent_1=malloc(sizeof(adjDT));
adjDT *adjacent_2=malloc(sizeof(adjDT));
adjacent_1->NODE.name=strdup(cur->src);
adjacent_2->NODE.name=strdup(cur->dest);
adjacent_2->following = NULL;
graph->adj = adjacent_1;
graph->adj->following=adjacent_2;
graph->down = NULL;
//graph.adj.node.name src is: %s\n",adjacent_1->NODE.name
start=graph;
mover=start;
transient=start;
graph->adj->following dest is: %s\n",graph->adj->following->NODE.name
mover->adj->NODE.name is %s\n",mover ->adj->NODE.name
}
else
{
counter=0;
mover=NULL;
mover=start; //check for existence of source node, always beginning from start
while(mover!=NULL)
//check till existence of source node in the adjacency list
{
if (strcmp(mover->adj->NODE.name,cur->src)==0)
//if source already exists, then destination to be added to this adjacency list
{
caught = mover;
counter =1;
break;
}
else{
if(mover->down!=NULL){
//search till end of graph,counter=0,implies source read first time
mover = mover->down;
}
else{
mover = NULL;}
counter =0;
}
}
if (caught!=NULL)
//assign traveller to adjacency list because source exists
{
traveller = caught->adj;
}
if(counter==1)
{

    if(strcmp(caught->adj->NODE.name,cur->src)==0)
//if node already exists with same source, creating a new node to add the current destination to the node
{
Not_exists =1;
while(traveller->following !=NULL)
{

//check if the node already exists in adjacency, if exists ignore
if(strcmp(traveller->following->NODE.name,cur->dest)==0)
{
Not_exists =0;
break;
}
traveller=traveller->following;
//traveller->NODE.name is %s\n",traveller->NODE.name
}
if(Not_exists==1)
//adding the node to adjacency list if it doesnt exist for that source
{
adjDT *adjacent=(adjDT *)malloc(sizeof(adjDT));
adjacent->NODE.name=strdup(cur->dest);
adjacent->following = NULL;
//adjacent->NODE.name is %s\n",adjacent->NODE.name
traveller->following = adjacent;
//traveller->following->NODE.name is %s\n",traveller->following->NODE.name
traveller=traveller->following;
//traveller->NODE.name is %s\n",traveller->NODE.name
}
}
}
else
{
//to create a new graph and move the graph pointer down
//there is a new source that was not added to graph object
graph1 = (gDT*)malloc(sizeof(gDT));
adjDT *adjacent1=malloc(sizeof(adjDT));
adjDT *adjacent2=malloc(sizeof(adjDT));
//creating a new object
adjacent1->NODE.name=strdup(cur->src);
adjacent2->NODE.name=strdup(cur->dest);
graph1->adj = adjacent1;
graph1->adj->following = adjacent2;
adjacent2->following = NULL;
graph1->down = NULL;

//graph->adj->NODE.name src is %s\n",graph1->adj->NODE.name
//graph->adj->NODE.name dest is %s\n",graph1->adj->following->NODE.name
transient->down = graph1;
transient = transient->down;
}
}
}
}

void display_graph(gDT *start)
{
adjDT *adjmover = NULL;
gDT *src_traverser=NULL;
//traversing the graph
printf("printing adjaceny list for each source\n");
for(src_traverser=start;src_traverser;src_traverser=src_traverser->down)
{
printf("source is %s\n",src_traverser->adj->NODE.name);
for(adjmover=src_traverser->adj; adjmover;adjmover=adjmover->following)
{
    printf(" %s-> ",adjmover->NODE.name);
}
printf("\n");
}
}

void out_degree(gDT *start)
{
adjDT *adjmover = NULL;
gDT *src_traverser=NULL;
int out_degree=0;
printf("printing out degree\n");
for(src_traverser=start;src_traverser;src_traverser=src_traverser->down)
{
out_degree=0;
for(adjmover=src_traverser->adj;adjmover;adjmover=adjmover->following)
{
out_degree++;
}
printf("out degree for %s is %d\n",src_traverser->adj->NODE.name,(out_degree)-1);
}
}
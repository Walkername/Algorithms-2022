package lesson6;

import kotlin.NotImplementedError;
import lesson6.impl.GraphBuilder;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        GraphBuilder spanningTree = new GraphBuilder();
        Set<Graph.Vertex> set = new HashSet<>();
        for (Graph.Vertex vertex : graph.getVertices()) {
            if (set.isEmpty()) {
                set.add(vertex);
            }
            for (Graph.Vertex neighbour : graph.getNeighbors(vertex)) {
                if (!set.contains(neighbour)) {
                    set.add(neighbour);
                    spanningTree.addVertex(vertex.getName());
                    spanningTree.addVertex(neighbour.getName());
                    spanningTree.addConnection(vertex, neighbour, 1);
                }
            }
        }
        return spanningTree.build();
    }
    // Трудоеёмкость - O(V+E), V - vertices, E - edges
    // Ресурсоёмкость - O(V), V - vertices

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     *        D, K, J, E, A, B, G, H, C, I, F
     */
    public static Path longestSimplePath(Graph graph) {
        Deque<Path> queuePaths = new ArrayDeque<>();
        for (Graph.Vertex vertex: graph.getVertices()) {
            queuePaths.push(new Path(vertex));              // put all vertices in a queue as class Path(vertex)
        }
        Path maxSimplePath = new Path();    // \ initialization of variable for future return
        int maxLength = 0;                  // /
        // empty the queue until all possible paths have been checked
        while (!queuePaths.isEmpty()) {
            Path currentPath = queuePaths.pop();        // getting new path or unfinished path
            if (currentPath.getLength() > maxLength) {  // comparison of current and max paths
                maxSimplePath = currentPath;
                maxLength = currentPath.getLength();
            }
            // continuation of path due to pushing of neighbours in queue
            for (Graph.Vertex neighbour: graph.getNeighbors(currentPath.getVertices().get(currentPath.getLength()))) {
                if (!currentPath.contains(neighbour)) {
                    queuePaths.push(new Path(currentPath, graph, neighbour));
                }
            }
        }
        return maxSimplePath;
    }
    // Трудоемкость - O(V) + O(V+E) = O(V+E)
    // Ресурсоемкость - O(V^2), V - vertices.
    // queuePaths contains all paths (number of paths = number of vertices) | \
    // Each path contains all vertices.                                         | / O(V) * O(V) = O(V^2)


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}

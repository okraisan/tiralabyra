package tiralabyra;

/**
 * FIFO queue of integers, using a circular buffer internally.
 */
public class Queue {
  private int size = 0;
  private int[] data;
  private int head = 0;

  public Queue() {
    data = new int[1];
  }

  /**
   * Initialize a FIFO queue with an internal circular buffer.
   * @param reservedSize Initial size of the internal container.
   */
  public Queue(int reservedSize) {
    if (reservedSize <= 0) {
      throw new RuntimeException("Reserved size must be greater than 0");
    }
    data = new int[reservedSize];
  }

  public int size() {
    return size;
  }

  /**
   * Double the size of the internal storage.
   */
  private void reallocateBigger() {
    int[] newData = new int[data.length * 2];
    for (int i = 0; i < size; i++) {
      newData[i] = data[(head + i) % data.length];
    }
    head = 0;
    data = newData;
  }

  /**
   * Insert a new node into the queue.
   */
  public void insert(int newnode) throws RuntimeException {
    if (size == data.length) {
      reallocateBigger();
    }

    size++;
    int tail = (head + size - 1) % data.length;
    data[tail] = newnode;
  }

  /**
   * Return and remove the first element in the queue.
   */
  public int pop() throws RuntimeException {
    if (size == 0) {
      throw new NullPointerException("Can't pop from empty queue");
    }

    int result = data[head];
    head = (head + 1) % data.length;
    size--;

    return result;
  }
}

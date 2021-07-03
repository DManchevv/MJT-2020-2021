package bg.sofia.uni.fmi.mjt.wish.list;

class CustomThread extends Thread {
    TodoistServer server;

    public CustomThread(TodoistServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.start();
    }

}
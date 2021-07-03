package bg.sofia.uni.fmi.mjt.wish.list;

class CustomThread extends Thread {
    WishListServer server;

    public CustomThread(WishListServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        server.start();
    }

}
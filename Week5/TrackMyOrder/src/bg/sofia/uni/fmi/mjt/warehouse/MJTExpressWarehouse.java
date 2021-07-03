package bg.sofia.uni.fmi.mjt.warehouse;

import bg.sofia.uni.fmi.mjt.warehouse.exceptions.CapacityExceededException;
import bg.sofia.uni.fmi.mjt.warehouse.exceptions.ParcelNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MJTExpressWarehouse<L, P> implements DeliveryServiceWarehouse<L, P> {
    /**
     * Creates a new instance of MJTExpressWarehouse with the given characteristics
     *
     * @param capacity        the total number of parcels that the warehouse can store
     * @param retentionPeriod the maximum number of days for which a parcel can stay in the warehouse,
     *                        counted from the day the parcel
     * was submitted. After that time passes, the parcel can be removed from the warehouse
     */
    private int capacity;
    private int retentionPeriod;
    Map<L, P> warehouse = new HashMap<>();
    Map<L, LocalDateTime> warehouseDates = new HashMap<>();

    public MJTExpressWarehouse(int capacity, int retentionPeriod) {
        this.capacity = capacity;
        this.retentionPeriod = retentionPeriod;
    }

    @Override
    public void submitParcel(L label, P parcel, LocalDateTime submissionDate) throws CapacityExceededException {
        if (label == null || parcel == null || submissionDate == null) {
            throw new IllegalArgumentException("One of the inputted arguments is null!");
        }

        if (submissionDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("The date is a date in the future!");
        }

        if (warehouse.size() == capacity) {
            boolean isFree = false;
            for (Map.Entry<L, LocalDateTime> entry : warehouseDates.entrySet()) {
                if (entry.getValue().plusDays(retentionPeriod).isBefore(LocalDateTime.now())) {
                    warehouseDates.remove(entry.getKey());
                    warehouse.remove(entry.getKey());
                    isFree = true;
                    break;
                }
            }
            if (!isFree) {
                throw new CapacityExceededException("Maximum capacity reached! Cannot add more parcels.");
            }
        }

        warehouse.put(label, parcel);
        warehouseDates.put(label, submissionDate);
    }

    @Override
    public P getParcel(L label) {
        if (label == null) {
            throw new IllegalArgumentException("Label is null!");
        }

        if (!warehouse.containsKey(label)) {
            return null;
        }

        return warehouse.get(label);
    }

    @Override
    public P deliverParcel(L label) throws ParcelNotFoundException {
        if (label == null) {
            throw new IllegalArgumentException("Label is null!");
        }

        if (!warehouse.containsKey(label)) {
            throw new ParcelNotFoundException("Parcel with the given label does not exist in the warehouse!");
        }

        P currentParcel = warehouse.get(label);
        warehouse.remove(label);
        warehouseDates.remove(label);
        return currentParcel;
    }

    @Override
    public double getWarehouseSpaceLeft() {
        double a = (Double.valueOf(this.capacity) - Double.valueOf(warehouse.size())) / Double.valueOf(this.capacity);
        return Math.round(a * 100.0) / 100.0;
    }

    @Override
    public Map<L, P> getWarehouseItems() {
        return warehouse;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedBefore(LocalDateTime before) {
        if (before == null) {
            throw new IllegalArgumentException("before is null!");
        }

        Map<L, P> parcelsBefore = new HashMap<>();
        List<L> keyList = new ArrayList<>();
        for (Map.Entry<L, LocalDateTime> entry : warehouseDates.entrySet()) {
            if (entry.getValue().isBefore(before)) {
                parcelsBefore.put(entry.getKey(), warehouse.get(entry.getKey()));
                keyList.add(entry.getKey());
            }
        }

        for (L currentKey : keyList) {
            warehouseDates.remove(currentKey);
            warehouse.remove(currentKey);
        }

        return parcelsBefore;
    }

    @Override
    public Map<L, P> deliverParcelsSubmittedAfter(LocalDateTime after) {
        if (after == null) {
            throw new IllegalArgumentException("before is null!");
        }

        Map<L, P> parcelsAfter = new HashMap<>();
        List<L> keyList = new ArrayList<>();
        for (Map.Entry<L, LocalDateTime> entry : warehouseDates.entrySet()) {
            if (entry.getValue().isAfter(after)) {
                parcelsAfter.put(entry.getKey(), warehouse.get(entry.getKey()));
                keyList.add(entry.getKey());
            }
        }

        for (L currentKey : keyList) {
            warehouseDates.remove(currentKey);
            warehouse.remove(currentKey);
        }

        return parcelsAfter;
    }
}

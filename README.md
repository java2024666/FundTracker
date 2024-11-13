# FundTracker

## 技術架構

- Spring Boot
- Spring Data JPA
- MySQL
- Maven


## API 文件

### 1. 抓取基金資料
POST /api/fetch
### 2. 查詢某日價格
GET /api/product/{productId}/datePrice?date={date}
### 3. 修改當日價格
PUT /api/product/{productId}/updatePrice?date={date}&price={price}
### 4. 新增價格
POST /api/product/{productId}/addPrice?date={date}&price={price}
### 5. 刪除當日價格
DELETE /api/product/{productId}/deletePrice?date={date}
### 6. 計算價格漲跌
GET /api/product/{productId}/priceChange?startDate={startDate}&endDate={endDate}
### 7. 計算漲跌幅
GET /api/product/{productId}/priceChangeRate?startDate={startDate}&endDate={endDate}



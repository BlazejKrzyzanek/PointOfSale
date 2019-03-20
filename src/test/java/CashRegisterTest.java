import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CashRegisterTest {

    private final String GOOD_BARCODE = "123123123";
    private ProductScanner productScannerMock;
    private LcdDisplay lcdDisplayMock;
    private OutputDevice outputDeviceMock;
    private Statement statementMock;
    private ResultSet resultSetMock;
    private CashRegister cashRegister;

    @BeforeEach
    void setUp() {
        productScannerMock = mock(ProductScanner.class);
        lcdDisplayMock = mock(LcdDisplay.class);
        outputDeviceMock = mock(OutputDevice.class);

        statementMock = mock(Statement.class, RETURNS_DEEP_STUBS);
        Connection connectionMock = mock(Connection.class, RETURNS_DEEP_STUBS);
        resultSetMock = mock(ResultSet.class, RETURNS_DEEP_STUBS);

        cashRegister = new CashRegister(productScannerMock, lcdDisplayMock, outputDeviceMock,
                statementMock, connectionMock, resultSetMock);
    }

    @Test
    void shouldAddValidProduct() throws SQLException {
        assertTrue(cashRegister.getProducts().isEmpty());

        when(productScannerMock.scanBarcode()).thenReturn(GOOD_BARCODE);
        when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getString("barcode")).thenReturn(GOOD_BARCODE);
        when(resultSetMock.getString("name")).thenReturn("sugar");
        when(resultSetMock.getString("price")).thenReturn("3.20");

        cashRegister.processProduct();
        assertEquals(1, cashRegister.getProducts().size());

        verify(productScannerMock, times(1)).scanBarcode();
        verify(lcdDisplayMock, times(1)).display(any(Product.class));

    }

    @Test
    void shouldntAddProductWithNotValidBarcode(){

        String NOT_VALID_BARCODE = "not valid";
        when(productScannerMock.scanBarcode()).thenReturn(NOT_VALID_BARCODE);

        cashRegister.processProduct();

        assertTrue(cashRegister.getProducts().isEmpty());
        verify(productScannerMock, times(1)).scanBarcode();
        verify(lcdDisplayMock, times(1)).print(anyString());  // displaying exception
    }

    @Test
    void shouldntAddProductWhichDoesNotExistInDatabase() throws SQLException {

        String NOT_EXIST_BARCODE = "777777777";
        when(productScannerMock.scanBarcode()).thenReturn(NOT_EXIST_BARCODE);
        when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(false);

        cashRegister.processProduct();

        assertTrue(cashRegister.getProducts().isEmpty());
        verify(productScannerMock, times(1)).scanBarcode();
        verify(lcdDisplayMock, times(1)).print(anyString());  // displaying exception
    }

    @Test
    void shouldPrintReceiptAndClearProductSOnExit() throws SQLException {

        when(productScannerMock.scanBarcode()).thenReturn(GOOD_BARCODE);
        when(statementMock.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getString("barcode")).thenReturn(GOOD_BARCODE);
        when(resultSetMock.getString("name")).thenReturn("sugar");
        when(resultSetMock.getString("price")).thenReturn("3.20");
        cashRegister.exit();
        verify(lcdDisplayMock).print(anyString());
        verify(outputDeviceMock).print(anyString());
        assertTrue(cashRegister.getProducts().isEmpty());
    }
}
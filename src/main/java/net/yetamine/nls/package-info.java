/**
 * A simple facade for National Language Support (NLS).
 *
 * <h1>Overview</h1>
 *
 * While {@link java.util.ResourceBundle} provides a common and widely used
 * means for NLS, it has some disadvantages. One of the major disadvantage is
 * limited extensibility and ability to change the behavior, although the SPI
 * {@link java.util.spi.ResourceBundleControlProvider} offers some flexibility.
 * The flexibility of the SPI, however, might not be sufficient within modular
 * frameworks like OSGi, that use a rich class loading model and do not rely on
 * limited {@link java.util.ServiceLoader}, when the resource provider is given
 * as a service.
 *
 * <p>
 * This facade provides a layer to hide more details of the resource resolution
 * process and offers a more fluent API.
 */
package net.yetamine.nls;

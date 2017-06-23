package org.interledger.codecs;

import org.interledger.InterledgerAddress;
import org.interledger.codecs.oer.OerIA5StringCodec;
import org.interledger.codecs.oer.OerIA5StringCodec.OerIA5String;
import org.interledger.codecs.oer.OerLengthPrefixCodec;
import org.interledger.codecs.oer.OerLengthPrefixCodec.OerLengthPrefix;
import org.interledger.codecs.oer.OerOctetStringCodec;
import org.interledger.codecs.oer.OerOctetStringCodec.OerOctetString;
import org.interledger.codecs.oer.OerUint32Codec;
import org.interledger.codecs.oer.OerUint32Codec.OerUint32;
import org.interledger.codecs.oer.OerUint64Codec;
import org.interledger.codecs.oer.OerUint64Codec.OerUint64;
import org.interledger.codecs.oer.OerUint8Codec;
import org.interledger.codecs.oer.OerUint8Codec.OerUint8;
import org.interledger.codecs.oer.ilp.InterledgerAddressOerCodec;
import org.interledger.codecs.oer.ilp.InterledgerPacketTypeOerCodec;
import org.interledger.codecs.oer.ilp.InterledgerPaymentOerCodec;
import org.interledger.codecs.oer.ilqp.QuoteByDestinationAmountRequestOerCodec;
import org.interledger.codecs.oer.ilqp.QuoteByDestinationAmountResponseOerCodec;
import org.interledger.codecs.oer.ilqp.QuoteBySourceAmountRequestOerCodec;
import org.interledger.codecs.oer.ilqp.QuoteBySourceAmountResponseOerCodec;
import org.interledger.codecs.packettypes.InterledgerPacketType;
import org.interledger.ilp.InterledgerPayment;
import org.interledger.ilqp.QuoteByDestinationAmountRequest;
import org.interledger.ilqp.QuoteByDestinationAmountResponse;
import org.interledger.ilqp.QuoteBySourceAmountRequest;
import org.interledger.ilqp.QuoteBySourceAmountResponse;

/**
 * A factory class for constructing a CodecContext that can read and write Interledger objects using
 * ASN.1 OER encoding.
 */
public class CodecContextFactory {

  /**
   * Create an instance of {@link CodecContext} that encodes and decodes Interledger packets using
   * ASN.1 OER encoding.
   */
  public static CodecContext interledger() {

    // OER Base...
    return new CodecContext()
        .register(OerUint8.class, new OerUint8Codec())
        .register(OerUint32.class, new OerUint32Codec())
        .register(OerUint64.class, new OerUint64Codec())
        .register(OerLengthPrefix.class, new OerLengthPrefixCodec())
        .register(OerIA5String.class, new OerIA5StringCodec())
        .register(OerOctetString.class, new OerOctetStringCodec())

        // ILP
        .register(InterledgerAddress.class, new InterledgerAddressOerCodec())
        .register(InterledgerPacketType.class, new InterledgerPacketTypeOerCodec())
        .register(InterledgerPayment.class, new InterledgerPaymentOerCodec())

        // ILQP
        .register(QuoteByDestinationAmountRequest.class,
            new QuoteByDestinationAmountRequestOerCodec())
        .register(QuoteByDestinationAmountResponse.class,
            new QuoteByDestinationAmountResponseOerCodec())
        .register(QuoteBySourceAmountRequest.class, new QuoteBySourceAmountRequestOerCodec())
        .register(QuoteBySourceAmountResponse.class, new QuoteBySourceAmountResponseOerCodec())
        //.register(QuoteLiquidityRequest.class, new QuoteLiquidityRequestOerCodec())
        //.register(QuoteLiquidityResponse.class, new QuoteLiquidityResponseOerCodec())

        ;
  }

  public static CodecContext interledgerJson() {
    throw new RuntimeException("Not yet implemented!");
  }

  public static CodecContext interledgerProtofbuf() {
    throw new RuntimeException("Not yet implemented!");
  }

}

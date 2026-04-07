package com.nscc.onlinestore2.controllers;


import com.nscc.onlinestore2.dto.CartDTO;
import com.nscc.onlinestore2.dto.CartItemDTO;
import com.nscc.onlinestore2.entity.Game;
import com.nscc.onlinestore2.service.GameService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173") // enables cors
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final GameService gameService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;


    @PostConstruct
    public void init() { Stripe.apiKey = stripeSecretKey; }

    public CheckoutController(GameService gameService) {
        this.gameService = gameService;


        //Stripe.apiKey = stripeSecretKey;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody CartDTO cart) throws StripeException
    {
        String YOUR_DOMAIN = "http://localhost:5173";

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                        .setUiMode(SessionCreateParams.UiMode.EMBEDDED)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setReturnUrl(YOUR_DOMAIN + "/confirmation?session_id={CHECKOUT_SESSION_ID}");

        for (CartItemDTO cartItem : cart.getItems()) {
            Game game = gameService.getGameById(cartItem.getId())
                    .orElseThrow(() -> new RuntimeException("Game not found"));

            //Changed price to Long from double
            Long price = game.getPrice();

            paramsBuilder.addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(cartItem.getQuantity())
                                    // Provide the exact Price ID of the product you want to sell
                                    // .setPrice("{{PRICE_ID}}")
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("cad")
                                                    .setUnitAmount(price)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData
                                                                    .builder()
                                                                    .setName(game.getName())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
        }

        SessionCreateParams params = paramsBuilder.build();
        Session session = Session.create(params);

        Map<String, String> map = new HashMap<>();
        map.put("clientSecret", session.getClientSecret());
        return map;
    }

    @GetMapping("session-status")
    public Map<String, String> getSessionStatus(@RequestParam("session_id") String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);

        Map<String, String> map = new HashMap<>();
        map.put("status", session.getStatus());
        map.put("customer_email", session.getCustomerDetails().getEmail());

        return map;
    }
}

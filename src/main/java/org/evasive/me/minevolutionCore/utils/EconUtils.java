package org.evasive.me.minevolutionCore.utils;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import net.ess3.api.MaxMoneyException;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class EconUtils {

    public static void addMoney(UUID uuid, float amount){
        try {
            Economy.add(uuid, BigDecimal.valueOf(amount));
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(Messages.parse("<green>+ $<amount></green>",  Placeholder.parsed("amount", String.valueOf(amount))));
        } catch (NoLoanPermittedException | MaxMoneyException | UserDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    public static void subtractMoney(UUID uuid, float amount){
        try{
            Economy.subtract(uuid, BigDecimal.valueOf(amount));
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage(Messages.parse("<red>- $<amount></red>",  Placeholder.parsed("amount", String.valueOf(amount))));
        } catch (MaxMoneyException | UserDoesNotExistException | NoLoanPermittedException e) {
            throw new RuntimeException(e);
        }
    }

    public static BigDecimal getMoney(UUID uuid){
        try {
            return Economy.getMoneyExact(uuid);
        } catch (UserDoesNotExistException e) {
            throw new RuntimeException(e);
        }
    }

}

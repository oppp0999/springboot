package com.example.firstboot

import org.apache.el.parser.AstBracketSuffix
import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Account (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String
    )

data class ViewAccount(
    val id:Long,
    val name:String
)

fun Account.toView()=
    ViewAccount(id, name)

interface AccountRepository: CrudRepository<Account, Long>{
    fun findByNameStartingWith(prefix:String): Iterable<Account>

    @org.springframework.data.jpa.repository.Query(
        "SELECT a FROM Account a WHERE a.name LIKE concat('%', :suffix)"
    )
    fun search(suffix: String): Iterable<Account>
}

data class CreateAccount(
    val name: String
)

@RestController
@RequestMapping("accounts")
class AccountsController(
    val accountRepository: AccountRepository
){
    @GetMapping
    fun findAll(): Iterable<ViewAccount> =
        accountRepository.search("Account").map{it.toView()}
    @PostMapping
    fun create(@RequestBody createAccount: CreateAccount): ViewAccount=
        accountRepository.save(
            Account(
                name=createAccount.name
            )
        ).toView()
}
$(document).ready(function () {
    $("#formulario").submit(function (event) {
        event.preventDefault();

        let produto = {
            nome: $('#nome').val(),
            categoria: $('#categoria').val(),
            quantidade: $('#quantidade').val(),
            preco: $('#preco').val(),
            descricao: $('#descricao').val()
        };

        $.get("http://localhost:3000/listagem-produtos", function (data) {
            let novoId;

            if (data.length > 0) {
                let ultimoProduto = data.slice(-1)[0];
                let transformId = parseInt(ultimoProduto.id, 10) + 1;
                novoId = transformId.toString();
            
            } else {
                novoId = "1";
            }

            produto.id = novoId;

            $.ajax({
                type: 'POST',
                url: 'http://localhost:3000/listagem-produtos',
                data: JSON.stringify(produto),
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function sucesso() {
                    alert('Produto cadastrado com sucesso!');
                    setTimeout(() => {
                        window.location.href = '/templates/listagem-produtos.html';
                    }, 0);
                },
                error: function falha() {
                    alert('Erro ao tentar cadastrar produto.');
                }
            });
        });
    });
});
// src/main/Main.java
package main;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import service.EstoqueService;
import service.RelatorioService;
import dao.ConnectionFactory;
import dao.UnidadeDAO;
import dao.MaterialDAO;
import dao.UsuarioDAO;
import beans.Unidade;
import beans.Material;
import beans.Usuario;
import beans.Perfil;

public class Main {
    public static void main(String[] args) {
        EstoqueService estoqueService = new EstoqueService();
        RelatorioService relatorioService = new RelatorioService();

        while (true) {
            String escolha = JOptionPane.showInputDialog(
                    null,
                    String.format(
                            "%n" +
                                    "════════════════════════════════════%n" +
                                    "   CONTROLE DE CONSUMO – v1%n" +
                                    "════════════════════════════════════%n" +
                                    "[1] Cadastros%n" +
                                    "[2] Entradas%n" +
                                    "[3] Saídas%n" +
                                    "[4] Inventário%n" +
                                    "[5] Relatórios%n" +
                                    "[6] Alertas%n" +
                                    "[0] Sair%n%n" +
                                    "Escolha: "),
                    "Menu",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (escolha == null || escolha.trim().equals("0")) break;

            switch (escolha.trim()) {
                case "1": { // Cadastros
                    String op = JOptionPane.showInputDialog(
                            null,
                            String.format(
                                    "%n" +
                                            "──────── Cadastros ────────%n" +
                                            "[1] Unidades – Cadastrar%n" +
                                            "[2] Unidades – Listar%n" +
                                            "[3] Materiais – Cadastrar%n" +
                                            "[4] Materiais – Listar%n" +
                                            "[5] Materiais – Atualizar Ponto de Reposição%n" +
                                            "[6] Usuários – Cadastrar%n" +
                                            "[7] Usuários – Listar%n" +
                                            "[8] Unidades – Excluir%n" +
                                            "[9] Materiais – Excluir%n" +
                                            "[10] Usuários – Excluir%n" +
                                            "[0] Voltar%n%n" +
                                            "Escolha: "),
                            "Cadastros",
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (op == null || op.trim().equals("0")) break;

                    switch (op.trim()) {
                        case "1": { // cadastrar unidade
                            String nome = JOptionPane.showInputDialog(null, "Nome da unidade:");
                            if (nome == null || nome.isBlank()) break;
                            try (Connection c = ConnectionFactory.getConnection()) {
                                long id = new UnidadeDAO().insert(c, new Unidade(null, nome.trim()));
                                JOptionPane.showMessageDialog(null, String.format("Unidade criada (ID=%d)", id));
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "2": { // listar unidades
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Unidade> list = new UnidadeDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Nenhuma unidade cadastrada."); break; }
                                StringBuilder sb = new StringBuilder();
                                sb.append(String.format("Unidades:%n"));
                                for (Unidade u : list) sb.append(String.format("%d - %s%n", u.getId(), u.getNome()));
                                JOptionPane.showMessageDialog(null, sb.toString());
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "3": { // cadastrar material
                            String codigo = JOptionPane.showInputDialog(null, "Código do material:"); if (codigo == null) break;
                            String desc = JOptionPane.showInputDialog(null, "Descrição:"); if (desc == null) break;
                            String unMed = JOptionPane.showInputDialog(null, "Unidade de medida (ex: ml, un):"); if (unMed == null) break;
                            String pontoStr = JOptionPane.showInputDialog(null, "Ponto de reposição (inteiro):"); if (pontoStr == null) break;
                            try (Connection c = ConnectionFactory.getConnection()) {
                                int ponto = Integer.parseInt(pontoStr.trim());
                                long id = new MaterialDAO().insert(c, new Material(null, codigo.trim(), desc.trim(), unMed.trim(), ponto));
                                JOptionPane.showMessageDialog(null, String.format("Material criado (ID=%d)", id));
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "4": { // listar materiais
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Material> list = new MaterialDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Nenhum material cadastrado."); break; }
                                StringBuilder sb = new StringBuilder();
                                sb.append(String.format("Materiais:%n"));
                                for (Material m : list) sb.append(String.format("%d - %s (%s)%n", m.getId(), m.getCodigo(), m.getDescricao()));
                                JOptionPane.showMessageDialog(null, sb.toString());
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "5": { // atualizar ponto de reposição
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Material> list = new MaterialDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Material."); break; }
                                String[] opts = list.stream().map(m -> String.format("%d - %s (%s)", m.getId(), m.getCodigo(), m.getDescricao())).toArray(String[]::new);
                                String esc = (String) JOptionPane.showInputDialog(null, "Selecione o Material:", "Materiais", JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                                if (esc == null) break;
                                long materialId = Long.parseLong(esc.split(" - ")[0]);
                                String novoStr = JOptionPane.showInputDialog(null, "Novo ponto de reposição:"); if (novoStr == null) break;
                                int novo = Integer.parseInt(novoStr.trim());
                                new MaterialDAO().updatePontoReposicao(c, materialId, novo);
                                JOptionPane.showMessageDialog(null, "Ponto de reposição atualizado.");
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "6": { // cadastrar usuário
                            String nome = JOptionPane.showInputDialog(null, "Nome do usuário:"); if (nome == null) break;
                            Perfil perfil = (Perfil) JOptionPane.showInputDialog(null, "Perfil:", "Perfil", JOptionPane.PLAIN_MESSAGE, null, Perfil.values(), Perfil.ATENDENTE);
                            if (perfil == null) break;
                            try (Connection c = ConnectionFactory.getConnection()) {
                                long id = new UsuarioDAO().insert(c, new Usuario(null, nome.trim(), perfil));
                                JOptionPane.showMessageDialog(null, String.format("Usuário criado (ID=%d)", id));
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "7": { // listar usuários
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Usuario> list = new UsuarioDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Nenhum usuário cadastrado."); break; }
                                StringBuilder sb = new StringBuilder();
                                sb.append(String.format("Usuários:%n"));
                                for (Usuario u : list) sb.append(String.format("%d - %s (%s)%n", u.getId(), u.getNome(), u.getPerfil()));
                                JOptionPane.showMessageDialog(null, sb.toString());
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "8": { // excluir unidade
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Unidade> list = new UnidadeDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Nenhuma unidade."); break; }
                                String[] opts = list.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                                String esc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                                if (esc == null) break;
                                long id = Long.parseLong(esc.split(" - ")[0]);
                                try {
                                    int rows = new UnidadeDAO().delete(c, id);
                                    JOptionPane.showMessageDialog(null, rows > 0 ? "Unidade excluída." : "Nada excluído.");
                                } catch (Exception fk) {
                                    JOptionPane.showMessageDialog(null, "Não é possível excluir: há referências (Estoque/Movimentações/Alertas).");
                                }
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "9": { // excluir material
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Material> list = new MaterialDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Nenhum material."); break; }
                                String[] opts = list.stream().map(m -> String.format("%d - %s (%s)", m.getId(), m.getCodigo(), m.getDescricao())).toArray(String[]::new);
                                String esc = (String) JOptionPane.showInputDialog(null, "Selecione o Material:", "Materiais", JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                                if (esc == null) break;
                                long id = Long.parseLong(esc.split(" - ")[0]);
                                try {
                                    int rows = new MaterialDAO().delete(c, id);
                                    JOptionPane.showMessageDialog(null, rows > 0 ? "Material excluído." : "Nada excluído.");
                                } catch (Exception fk) {
                                    JOptionPane.showMessageDialog(null, "Não é possível excluir: há referências (Estoque/Movimentações/Alertas).");
                                }
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "10": { // excluir usuário
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Usuario> list = new UsuarioDAO().listAll(c);
                                if (list.isEmpty()) { JOptionPane.showMessageDialog(null, "Nenhum usuário."); break; }
                                String[] opts = list.stream().map(u -> String.format("%d - %s (%s)", u.getId(), u.getNome(), u.getPerfil())).toArray(String[]::new);
                                String esc = (String) JOptionPane.showInputDialog(null, "Selecione o Usuário:", "Usuários", JOptionPane.PLAIN_MESSAGE, null, opts, opts[0]);
                                if (esc == null) break;
                                long id = Long.parseLong(esc.split(" - ")[0]);
                                try {
                                    int rows = new UsuarioDAO().delete(c, id);
                                    JOptionPane.showMessageDialog(null, rows > 0 ? "Usuário excluído." : "Nada excluído.");
                                } catch (Exception fk) {
                                    JOptionPane.showMessageDialog(null, "Não é possível excluir: há referências em Movimentações.");
                                }
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        default: JOptionPane.showMessageDialog(null, "Opção inválida.");
                    }
                    break;
                }

                case "2": { // Entradas
                    try (Connection c = ConnectionFactory.getConnection()) {
                        List<Unidade> unidades = new UnidadeDAO().listAll(c);
                        if (unidades.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Unidade."); break; }
                        String[] uopts = unidades.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                        String uEsc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, uopts, uopts[0]);
                        if (uEsc == null) break; long unidadeId = Long.parseLong(uEsc.split(" - ")[0]);

                        List<Material> mats = new MaterialDAO().listAll(c);
                        if (mats.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Material."); break; }
                        String[] mopts = mats.stream().map(m -> String.format("%d - %s (%s)", m.getId(), m.getCodigo(), m.getDescricao())).toArray(String[]::new);
                        String mEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Material:", "Materiais", JOptionPane.PLAIN_MESSAGE, null, mopts, mopts[0]);
                        if (mEsc == null) break; long materialId = Long.parseLong(mEsc.split(" - ")[0]);

                        String qtdStr = JOptionPane.showInputDialog(null, "Quantidade (inteiro):"); if (qtdStr == null) break; int qtd = Integer.parseInt(qtdStr.trim());

                        List<Usuario> usrs = new UsuarioDAO().listAll(c);
                        if (usrs.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Usuário."); break; }
                        String[] sopts = usrs.stream().map(u -> String.format("%d - %s (%s)", u.getId(), u.getNome(), u.getPerfil())).toArray(String[]::new);
                        String sEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Usuário:", "Usuários", JOptionPane.PLAIN_MESSAGE, null, sopts, sopts[0]);
                        if (sEsc == null) break; long usuarioId = Long.parseLong(sEsc.split(" - ")[0]);

                        estoqueService.registrarEntrada(unidadeId, materialId, qtd, usuarioId);
                        JOptionPane.showMessageDialog(null, "Entrada registrado!");
                    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                    break;
                }

                case "3": { // Saídas
                    try (Connection c = ConnectionFactory.getConnection()) {
                        List<Unidade> unidades = new UnidadeDAO().listAll(c);
                        if (unidades.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Unidade."); break; }
                        String[] uopts = unidades.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                        String uEsc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, uopts, uopts[0]);
                        if (uEsc == null) break; long unidadeId = Long.parseLong(uEsc.split(" - ")[0]);

                        List<Material> mats = new MaterialDAO().listAll(c);
                        if (mats.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Material."); break; }
                        String[] mopts = mats.stream().map(m -> String.format("%d - %s (%s)", m.getId(), m.getCodigo(), m.getDescricao())).toArray(String[]::new);
                        String mEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Material:", "Materiais", JOptionPane.PLAIN_MESSAGE, null, mopts, mopts[0]);
                        if (mEsc == null) break; long materialId = Long.parseLong(mEsc.split(" - ")[0]);

                        String qtdStr = JOptionPane.showInputDialog(null, "Quantidade (inteiro):"); if (qtdStr == null) break; int qtd = Integer.parseInt(qtdStr.trim());

                        List<Usuario> usrs = new UsuarioDAO().listAll(c);
                        if (usrs.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Usuário."); break; }
                        String[] sopts = usrs.stream().map(u -> String.format("%d - %s (%s)", u.getId(), u.getNome(), u.getPerfil())).toArray(String[]::new);
                        String sEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Usuário:", "Usuários", JOptionPane.PLAIN_MESSAGE, null, sopts, sopts[0]);
                        if (sEsc == null) break; long usuarioId = Long.parseLong(sEsc.split(" - ")[0]);

                        String motivo = JOptionPane.showInputDialog(null, "Motivo (Uso/Perda/Vencimento):"); if (motivo == null) break;
                        estoqueService.registrarSaida(unidadeId, materialId, qtd, usuarioId, motivo);
                        JOptionPane.showMessageDialog(null, "Saída registrada!");
                    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                    break;
                }

                case "4": { // Inventário
                    try (Connection c = ConnectionFactory.getConnection()) {
                        List<Unidade> unidades = new UnidadeDAO().listAll(c);
                        if (unidades.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Unidade."); break; }
                        String[] uopts = unidades.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                        String uEsc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, uopts, uopts[0]);
                        if (uEsc == null) break; long unidadeId = Long.parseLong(uEsc.split(" - ")[0]);

                        List<Material> mats = new MaterialDAO().listAll(c);
                        if (mats.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Material."); break; }
                        String[] mopts = mats.stream().map(m -> String.format("%d - %s (%s)", m.getId(), m.getCodigo(), m.getDescricao())).toArray(String[]::new);
                        String mEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Material:", "Materiais", JOptionPane.PLAIN_MESSAGE, null, mopts, mopts[0]);
                        if (mEsc == null) break; long materialId = Long.parseLong(mEsc.split(" - ")[0]);

                        String contStr = JOptionPane.showInputDialog(null, "Contagem física:"); if (contStr == null) break; int contagem = Integer.parseInt(contStr.trim());

                        List<Usuario> usrs = new UsuarioDAO().listAll(c);
                        if (usrs.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Usuário."); break; }
                        String[] sopts = usrs.stream().map(u -> String.format("%d - %s (%s)", u.getId(), u.getNome(), u.getPerfil())).toArray(String[]::new);
                        String sEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Usuário:", "Usuários", JOptionPane.PLAIN_MESSAGE, null, sopts, sopts[0]);
                        if (sEsc == null) break; long usuarioId = Long.parseLong(sEsc.split(" - ")[0]);

                        int diff = estoqueService.reconciliarInventario(unidadeId, materialId, contagem, usuarioId);
                        JOptionPane.showMessageDialog(null, diff == 0 ? "Sem diferenças" : String.format("Ajuste aplicado: %d", diff));
                    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                    break;
                }

                case "5": { // Relatórios
                    String op = JOptionPane.showInputDialog(
                            null,
                            String.format(
                                    "%n" +
                                            "──────── Relatórios ────────%n" +
                                            "[1] Saldo por Unidade%n" +
                                            "[2] Consumo por Período (por material)%n" +
                                            "[0] Voltar%n%n" +
                                            "Escolha: "),
                            "Relatórios",
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (op == null || op.trim().equals("0")) break;
                    switch (op.trim()) {
                        case "1": { // saldo por unidade
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Unidade> unidades = new UnidadeDAO().listAll(c);
                                if (unidades.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Unidade."); break; }
                                String[] uopts = unidades.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                                String uEsc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, uopts, uopts[0]);
                                if (uEsc == null) break; long unidadeId = Long.parseLong(uEsc.split(" - ")[0]);
                                String rel = relatorioService.saldoPorUnidade(unidadeId);
                                JOptionPane.showMessageDialog(null, rel);
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        case "2": { // consumo por período
                            try (Connection c = ConnectionFactory.getConnection()) {
                                List<Unidade> unidades = new UnidadeDAO().listAll(c);
                                if (unidades.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Unidade."); break; }
                                String[] uopts = unidades.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                                String uEsc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, uopts, uopts[0]);
                                if (uEsc == null) break; long unidadeId = Long.parseLong(uEsc.split(" - ")[0]);

                                List<Material> mats = new MaterialDAO().listAll(c);
                                if (mats.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Material."); break; }
                                String[] mopts = mats.stream().map(m -> String.format("%d - %s (%s)", m.getId(), m.getCodigo(), m.getDescricao())).toArray(String[]::new);
                                String mEsc = (String) JOptionPane.showInputDialog(null, "Selecione o Material:", "Materiais", JOptionPane.PLAIN_MESSAGE, null, mopts, mopts[0]);
                                if (mEsc == null) break; long materialId = Long.parseLong(mEsc.split(" - ")[0]);

                                String iniStr = JOptionPane.showInputDialog(null, "Data inicial (YYYY-MM-DD):"); if (iniStr == null) break; LocalDate ini = LocalDate.parse(iniStr.trim());
                                String fimStr = JOptionPane.showInputDialog(null, "Data final (YYYY-MM-DD):"); if (fimStr == null) break; LocalDate fim = LocalDate.parse(fimStr.trim());
                                String rel = relatorioService.consumoPorPeriodo(unidadeId, materialId, ini, fim);
                                JOptionPane.showMessageDialog(null, rel);
                            } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                            break;
                        }
                        default: JOptionPane.showMessageDialog(null, "Opção inválida.");
                    }
                    break;
                }

                case "6": { // Alertas
                    try (Connection c = ConnectionFactory.getConnection()) {
                        List<Unidade> unidades = new UnidadeDAO().listAll(c);
                        if (unidades.isEmpty()) { JOptionPane.showMessageDialog(null, "Cadastre ao menos 1 Unidade."); break; }
                        String[] uopts = unidades.stream().map(u -> String.format("%d - %s", u.getId(), u.getNome())).toArray(String[]::new);
                        String uEsc = (String) JOptionPane.showInputDialog(null, "Selecione a Unidade:", "Unidades", JOptionPane.PLAIN_MESSAGE, null, uopts, uopts[0]);
                        if (uEsc == null) break; long unidadeId = Long.parseLong(uEsc.split(" - ")[0]);
                        List<String> msgs = estoqueService.gerarAlertasReposicao(unidadeId);
                        JOptionPane.showMessageDialog(null, msgs.isEmpty() ? "Sem alertas no momento" : String.join("\n", msgs));
                    } catch (Exception e) { JOptionPane.showMessageDialog(null, e.getMessage()); }
                    break;
                }

                default: JOptionPane.showMessageDialog(null, "Opção inválida.");
            }
        }
    }
}

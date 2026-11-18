# ISSUES — Portugol Studio · Open Issue Backlog & TODO List

> **Snapshot date:** 2026-11-17
> **Backlog size:** 243 open issues + 3 open pull requests
> **Repository audited:** [`UNIVALI-LITE/Portugol-Studio`](https://github.com/UNIVALI-LITE/Portugol-Studio) (upstream)
> **This fork:** [`havaianasdestruido/Portugol-Studio`](https://github.com/havaianasdestruido/Portugol-Studio)

---

## 0. Scope, source and method

**Which issues are listed here.** The link given for this task points at
`havaianasdestruido/Portugol-Studio`. That repository is a **fork with the Issues tracker
disabled** (`has_issues: false`, 0 open issues, 0 open PRs — confirmed through the GitHub API),
so it has no backlog of its own. All 243 open issues and 3 open pull requests in this document
therefore come from the **upstream** repository `UNIVALI-LITE/Portugol-Studio`, which is the
project this checkout tracks (`origin` = the fork, base commit `5640a2e` = upstream `master`).

**How the backlog was triaged.**

1. Every open issue was pulled with its labels, milestone, comment count and body.
2. The 96 issues whose title matches `Erro numero <sha1>` are **auto-generated crash reports**
   emitted by the IDE's "report bug" button. Deduplicated by stack-trace hash and then grouped by the
   first application stack frame, the **84 distinct hashes collapse into 20 families**, plus 28
   reports that carry no trace at all (appendix B).
3. The remaining 147 issues were read individually and grouped by theme.
4. Each issue is owned by exactly **one** item (appendix A proves the 243 → 46 mapping is complete
   and duplicate-free); related symptoms are cross-referenced rather than double-counted.
5. For every "possible fix" below, the relevant code path was located in this checkout
   (`core/`, `ide/`, `console/`) and the reference is given as `file:line`. Claims marked
   *verified* were checked by reading the source; claims marked *hypothesis* still need
   reproduction.
6. Every one of the 243 issues appears exactly once in appendix A/C — a machine-checked index.

**A note on the auto-generated reports.** 28 of the 96 crash reports contain only the issue
template (`[pressione o botao "copiar erro"...]`) — the user never pasted the stack trace, so they
carry **no technical information at all**. Fixing the reporter itself is item **PS-060**; until
then this group will keep growing and drowning the real reports.

---

## 1. How to read this document

### Priority

| Tag | Meaning | Rule of thumb |
|-----|---------|---------------|
| **P0** | Blocker — user work is lost, the app cannot start, or automation/CI is silently broken | Fix before any release. Should be a hotfix branch. |
| **P1** | Critical — a core feature is broken for a large share of users, or an entire OS/group is locked out | Target the next minor release. |
| **P2** | Normal — real bug or quality gap affecting a subset of users; workarounds exist | Backlog of the next-but-one release. |
| **P3** | Low — enhancement, polish, docs, niche audience | Opportunistic / `good first issue`. |

### Difficulty

| Tag | Meaning | Typical effort |
|-----|---------|----------------|
| **MAX** | Cross-cutting: build system, packaging, architecture, OS-specific signing, or a language-grammar change | > 3 days, needs design + QA on 2+ operating systems |
| **MED** | Touches several files or an analyzer/generator path; needs tests and manual QA | 1–3 days |
| **MIN** | Localized change, low regression risk, often a null-guard or a one-line fix | ≤ half a day |

### Status legend

`open` · `needs-repro` (cannot be reproduced with the info given) · `blocked` (waiting on another item) · `has-PR`

> **Effort note for a maintainer working alone:** items tagged `MAX` that require macOS hardware
> (code signing, notarization, `.dmg` generation) are effectively **blocked** unless a Mac-based
> contributor or CI runner is available. They are still listed, because they are the single
> largest source of install failures.

---

## 2. Executive summary

| Area | Items | Open issues owned | Share |
|------|:-----:|:-----------------:|:-----:|
| **A** · Data loss & startup blockers (4 × P0) | 5 | 54 | 22.2 % |
| **B** · Platform & distribution (macOS, Linux, installer, Java) | 6 | 36 | 14.8 % |
| **C** · Core language: lexer, parser, semantics, codegen | 14 | 36 | 14.8 % |
| **D** · Runtime libraries (Gráficos, Util, Texto, objetos) | 6 | 37 | 15.2 % |
| **E** · Editor, inspector, console & UI | 9 | 46 | 18.9 % |
| **F** · Infrastructure, tooling & docs | 5 | 29 | 11.9 % |
| **G** · Not actionable as filed | 1 | 5 | 2.1 % |
| **Total** | **46** | **243** | **100 %** |

*Counts are generated from the API snapshot — see appendix A for the per-item breakdown and
appendix C for the issue-by-issue index.*

### The five things to fix first

1. **PS-001 — the app does not start.** A single `null` (the mouse pointer is outside every screen)
   kills the whole start-up. **12 issues**, newest in 2026, and the fix is a null-guard.
2. **PS-002 / PS-003 — saving crashes or silently does nothing.** **39 issues** between the two
   items — by far the most-reported problem in the tracker, and it destroys student work.
3. **PS-010 — macOS is barely usable:** the `.dmg` is reported as "corrupted" since 2.7.3, Catalina+
   hides the filesystem from the IDE. **10 issues**; PR #1097 already has the workaround.
4. **PS-011 — Java 8 + WebLAF** is the shared root cause of the ghosting/duplication family
   (#421, #1064, #1206), the start-up `NoClassDefFound`s, the 4K font problem and the broken
   multi-monitor behaviour. **11 issues.**
5. **PS-021 — dynamic array sizes** (`inteiro v[n]`) is the top language gap and makes the desktop
   IDE disagree with Portugol WebStudio. **6 issues**; **PR #1220 is already written**.

### The shape of the backlog

* **96 of 243 issues (40 %) are auto-generated crash reports**, not human bug reports. They collapse
  into **20 stack-trace families** (appendix B). The **five largest families (53 reports, 55 % of the
  group) all map onto three items: PS-002 (28), PS-001 (10) and PS-045 (15)**. Another **28 have no
  stack trace at all**.
* Only **65 issues carry any `bug` label** (61 exactly `bug`) out of 243, and **149 carry no label
  whatsoever**; 13 are `couldn't reproduce` and 9 `no-response`, and **37 were opened in 2025–2026**
  — the backlog is still growing. Triage effort is better spent on the few families than on the
  individual reports — see **PS-060**.
* **Four items are P0** (PS-001, PS-002, PS-003, PS-005) covering **52 issues**; PS-050, though
  classified P1, has the same practical effect (the program runs, the user sees nothing) and should
  ride the same hotfix.
* The **median report is a beginner in a classroom** whose program "does not run" — in most cases
  because of PS-050 (console tab stuck on *Mensagens*) or PS-002/PS-003 (save failed silently).
  Those two items would resolve the majority of the unanswered reports in section E.

### Open pull requests (merge candidates)

| PR | Title | Relates to |
|----|-------|-----------|
| [#1220](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/1220) | `feat: suporte a tamanho dinâmico de vetores e matrizes` | **PS-021** (P1) — implements runtime-sized arrays in local scope |
| [#1097](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/1097) | `Aumentar a compatibilidade do Portugol-Studio com o macOS` | **PS-010** (P1) — works around the broken `.dmg` |
| [#985](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/985) | `Improve GRADLE build performance` | **PS-061** (P1) — parallel test execution, a prerequisite for the regression suite |

---

## 3. The TODO list

**46 work items**, ordered by priority inside each area. Tick a box when the fix is merged — the
issues it closes are listed at the end of the line and enumerated in appendix A. The detailed card
for every item (root cause with `file:line`, concrete fix steps, acceptance criteria) is in §4.

| Priority | Items | Difficulty split |
|----------|:-----:|------------------|
| **P0** | 5 | 2 MED, 3 MIN |
| **P1** | 16 | 4 MAX, 8 MED, 4 MIN |
| **P2** | 19 | 1 MAX, 9 MED, 9 MIN |
| **P3** | 6 | 1 MED, 5 MIN |

### Master checklist


**A · Data loss & startup blockers**

- [ ] **PS-001** `P0` `MIN` — App does not start — NullPointerException while maximizing the main window — 12 issues
- [ ] **PS-002** `P0` `MIN` — Saving a file throws NullPointerException / FileNotFoundException — 31 issues
- [ ] **PS-003** `P0` `MED` — Save appears to succeed but nothing is written (silent no-op) — 8 issues
- [ ] **PS-005** `P0` `MIN` — portugol-console -no-wait exits with code 0 on runtime errors and prints no message — 1 issue
- [ ] **PS-004** `P1` `MED` — Recovered-temporary-files dialog floods the IDE on every start — 2 issues

**B · Platform & distribution**

- [ ] **PS-010** `P1` `MAX` — macOS: .dmg reported as corrupted, Catalina+ hides files, crashes on Big Sur/Monterey — 10 issues
- [ ] **PS-011** `P1` `MAX` — Upgrade from Java 8 to a supported JDK and from WebLAF to a current release — 11 issues
- [ ] **PS-012** `P2` `MED` — Linux distribution: no Flatpak, no standalone package, no aarch64/ARM build — 3 issues
- [ ] **PS-013** `P2` `MED` — Installer problems: silent install, custom target directory, failing uninstaller — 7 issues
- [ ] **PS-015** `P2` `MED` — Linux: hangs, freeze on start-up and "must reboot to reopen" — 4 issues
- [ ] **PS-014** `P3` `MIN` — CRLF line endings break the Linux post-install scripts — 1 issue

**C · Core language**

- [ ] **PS-020** `P1` `MED` — escolha rejects cadeia, but the built-in help documents cadeia — 3 issues
- [ ] **PS-021** `P1` `MAX` — Vector/matrix size must be a literal or a constant — variables rejected — 6 issues
- [ ] **PS-023** `P1` `MIN` — sorteia(min, max) inside a vector initialiser throws NullPointerException — 3 issues
- [ ] **PS-024** `P1` `MIN` — Escape sequences do not work in caracter literals ('\n', '\t', '\'') — 1 issue
- [ ] **PS-027** `P1` `MED` — ClassCastException / NullPointerException when a vector or matrix reference is used as an argument — 4 issues
- [ ] **PS-028** `P1` `MED` — Parser: ^= inside para, and } inside strings/comments — 3 issues
- [ ] **PS-022** `P2` `MAX` — A function cannot declare a vector/matrix return type — 1 issue
- [ ] **PS-025** `P2` `MIN` — Concatenating a logico writes true/false instead of verdadeiro/falso — 1 issue
- [ ] **PS-026** `P2` `MED` — 'A' + 'B' is type-checked as cadeia but evaluated as an integer — 1 issue
- [ ] **PS-029** `P2` `MED` — vetor[i]++ followed by escreva produces a semantic error — 2 issues
- [ ] **PS-030** `P2` `MIN` — inicio() accepts a return type; starting the graphics mode twice throws NPE — 3 issues
- [ ] **PS-031** `P2` `MIN` — Integer overflow is silent, and -2147483648 is rejected although it is advertised — 3 issues
- [ ] **PS-032** `P2` `MED` — Repeated compilation fails: ClassFormatError, "Erro na compilação" for valid code — 3 issues
- [ ] **PS-033** `P2` `MIN` — Two NullPointerExceptions in the compiler pipeline — 2 issues

**D · Runtime libraries**

- [ ] **PS-042** `P1` `MIN` — Object library corrupts addresses when objects are freed — 1 issue
- [ ] **PS-043** `P1` `MED` — escreva breaks on non-UTF-8 consoles and mixes \n handling — 3 issues
- [ ] **PS-040** `P2` `MIN` — 3-digit hexadecimal colours are expanded to the wrong value — 1 issue
- [ ] **PS-041** `P2` `MIN` — manter_visivel = falso still forces the window on top — 1 issue
- [ ] **PS-045** `P2` `MED` — Index/selection arithmetic crashes: IndexOutOfBounds, NPE in the editor and file chooser — 20 issues
- [ ] **PS-044** `P3` `MIN` — Library-level issues: sorteia duplication, Util/Arquivos organisation, . completion — 11 issues

**E · Editor, inspector, console & UI**

- [ ] **PS-050** `P0` `MED` — Console tab stays on "Mensagens" and cannot be switched — program output invisible — 6 issues
- [ ] **PS-051** `P1` `MED` — Variable inspector shows stale/blank values for arrays and matrices — 6 issues
- [ ] **PS-053** `P1` `MIN` — New documents open with the "Olá Mundo" example instead of an empty file — 4 issues
- [ ] **PS-057** `P1` `MED` — "It doesn't work" reports that need a reproducer — 6 issues
- [ ] **PS-052** `P2` `MED` — Dialog/notification rendering: ghosting, duplicated UI, tiny fonts, monitor handling — 2 issues
- [ ] **PS-054** `P2` `MED` — Editor text input is corrupted on some Linux keyboard layouts / IMEs — 4 issues
- [ ] **PS-056** `P2` `MIN` — Help viewer, themes and tab labels break — 3 issues
- [ ] **PS-055** `P3` `MED` — Missing basic editor features: auto-indent, multi-cursor, RTF copy, Nerd-font/plugin keys — 14 issues
- [ ] **PS-058** `P3` `MIN` — Anti-aliasing/selection contrast in the dark theme — 1 issue

**F · Infrastructure, tooling & docs**

- [ ] **PS-060** `P1` `MED` — The bug reporter files issues with no information, then abandons them — 26 issues
- [ ] **PS-061** `P1` `MAX` — There is no automated test suite protecting the compiler — no issue filed (see card)
- [ ] **PS-062** `P2` `MIN` — Documentation: wrong escolha example, stale download links, update check — 2 issues
- [ ] **PS-064** `P2` `MIN` — Silent failures and misleading messages elsewhere — 1 issue
- [ ] **PS-063** `P3` `MIN` — Distribution & repository hygiene — no issue filed (see card)

**G · Not actionable as filed**

- [ ] **PS-070** `P3` `MIN` — Non-issues, questions and test submissions — 5 issues

---

## 4. Detailed cards

Each card has **Details** (what the user sees), **Root cause** (*verified* = confirmed by reading
this checkout, *hypothesis* = still needs reproduction), **Possible fix** (steps with `file:line`),
and **Acceptance criteria**.
### A · Data loss & startup blockers

### PS-001 · App does not start — `NullPointerException` while maximizing the main window
`P0` · `MIN` · **Labels:** `bug`, `JAVA system support` · **Status:** open

**Issues:** #914 · #1219 · #1216 · #1215 · #1214 · #1200 · #1186 · #1185 · #1154 · #1072 · #1063 · #1015
*(12 reports: hash `8b2cef0a…` × 9 and `8b876469…` × 2 carry the `Lancador.maximize` trace; #1015
carries the `8b2cef0a…` hash but no paste, so it is grouped here by hash rather than with PS-060)*

**Details.** The splash screen disappears and a dialog says *"Não foi possível iniciar o
Portugol Studio"*, with `Caused by: java.lang.NullPointerException at
br.univali.ps.ui.Lancador.maximize(Lancador.java:165)`. It is the most frequently filed crash in
the tracker and it has been reported continuously from 2022 to 2026. Users cannot reach the editor
at all; reinstall usually does not help.

**Root cause (*verified*).** `PortugolStudio.exibirTelaPrincipal()` calls
`Lancador.getInstance().maximize(true)` while still inside a `SwingUtilities.invokeAndWait(...)`
block, and converts *any* exception thrown there into a fatal `ExcecaoAplicacao`. `maximize()`
starts with `MouseInfo.getPointerInfo().getDevice()`
(`ide/src/main/java/br/univali/ps/ui/Lancador.java:165`), and `MouseInfo.getPointerInfo()` returns
`null` when the pointer is outside every screen — typical for RDP/remote sessions, headless
X servers, VMs with a resized display, some Wayland/`Xvfb` setups and macOS spaces being switched
during start-up. The result is an NPE that kills the whole start-up sequence.

**Possible fix.**
1. In `Lancador.maximize()` / `configurarMaximizar()` / `snapToEdge()`, replace
   `MouseInfo.getPointerInfo().getDevice()` with a helper that falls back to
   `GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()` and finally to
   `frame.getGraphicsConfiguration().getDevice()` when the pointer is `null`.
2. In `PortugolStudio.exibirTelaPrincipal()` (`ide/src/main/java/br/univali/ps/nucleo/PortugolStudio.java:898-926`)
   wrap the window-decoration calls (`setUndecorated`, `pack`, `maximize`) in their own
   `try/catch (RuntimeException)` and log instead of aborting: a failure to *maximize* must never
   prevent the IDE from opening.
3. Add a `--no-maximize` / `-Dsemmaximizar` escape hatch in `Lancador.main` for users in broken
   graphical environments.

**Acceptance criteria.** With `MouseInfo.getPointerInfo()` forced to return `null` (unit test or
`-Dsun.java2d` stub), the IDE opens in a normal window instead of failing; no
`ExcecaoAplicacao` is produced by the maximize path.

---

### PS-002 · Saving a file throws `NullPointerException` / `FileNotFoundException`
`P0` · `MIN` · **Labels:** `bug`, `Save bugs` · **Status:** open

**Issues (28 crash reports, all with a `salvaArquivo` frame):** #992 · #1008 · #1009 · #1014 ·
#1018 · #1023 · #1027 · #1041 · #1053 · #1058 · #1076 · #1082 · #1089 · #1122 · #1124 · #1125 ·
#1127 · #1130 · #1134 · #1135 · #1138 · #1169 · #1188 · #1189 · #1191 · #1194 · #1195 · #1212
**Also in this family (prose reports, no `Erro numero` title):** #808 *(NPE on save)* ·
#979 *(Ubuntu, `Permissão negada`)*

> Separately, **28 other auto-generated reports have no stack trace at all** and cannot be
> attributed to any code path — they are triaged under **PS-060** (fix the reporter) and shown in
> the last row of appendix B.

**Details.** Ctrl+S (or the toolbar button) produces a stack trace and, in most reports, the file
is **not** written. Two shapes appear: `NullPointerException` and
`java.io.FileNotFoundException … (Permissão negada)`. The crash frames are always
`AbaCodigoFonte.salvaArquivo(AbaCodigoFonte.java:928 or :932)` →
`FileHandle.save(FileHandle.java:19 or :21)`.

**Root cause (*verified*).** `AbaCodigoFonte.salvaArquivo()`
(`ide/src/main/java/br/univali/ps/ui/abas/AbaCodigoFonte.java:913-931`) checks
`documento.getFile() != null`, then calls `FileHandle.save(texto, getArquivoComExtensao(...))`.
There is no check that the target is **writable**, that the parent directory still exists (the
temp folder or a USB stick may have vanished), or that the file is not locked by another process
(Windows antivirus / OneDrive). The `catch (Exception)` at the end does nothing but hand the
exception to `TratadorExcecoes`, which shows the raw Java stack trace to a beginner.

**Possible fix.**
1. Before writing: `Objects.requireNonNull`, `Files.isWritable(parent)`,
   `parent.exists()`; if the test fails, fall back to a **Save As** dialog instead of throwing.
2. Write atomically — `Files.createTempFile` in the same directory, copy bytes, then
   `Files.move(..., ATOMIC_MOVE, REPLACE_EXISTING)` — so an interrupted write cannot truncate the
   user's file.
3. Keep the document's `changed` flag (`documento.setChanged(false)`) **only after** a successful
   write; today it is set inside the `try` after `FileHandle.save`, which is correct, but the flag
   is also cleared elsewhere on close.
4. Replace the raw stack trace with a translated dialog: *"Não foi possível gravar em
   `<path>`. O arquivo está protegido, em uso por outro programa, ou a pasta não existe."*

**Acceptance criteria.** With a read-only target file, Ctrl+S opens a "Save As" dialog and the
editor still shows the dirty marker; no Java stack trace is ever shown for an I/O failure.

---

### PS-003 · Save appears to succeed but nothing is written (silent no-op)
`P0` · `MED` · **Labels:** `bug`, `Save bugs`, `no-response` · **Status:** open

**Issues:** #961 · #969 · #1002 · #1028 · #1171 · #989 · #793 · #1203

**Details.** Distinct from PS-002: *no* error appears at all. The user clicks save (or presses
Ctrl+S) and nothing happens — no dialog, no file, no feedback. #1028: *"O botão ou com o comando
ctrl+s não funcionam"*; #969: *"simplesmente não aparece nada nenhuma aba pra salvar"*; #989:
files whose names contain digits never appear anywhere. #793 is the permission-denied variant on
Linux, already classified with a `treatable OS error` label by the maintainers.

**Root cause (*verified*).** `salvaArquivo()` is guarded by a private field `podeSalvar`
(`AbaCodigoFonte.java:124`, set to `false` in `:2579` and `true` in `:2389`). When the guard is
`false` the whole method body is skipped **silently**, and the corresponding `Action` is still
enabled in the UI. Any tab created through a path that forgets to set `podeSalvar = true`
(read-only tabs, recovery tabs, tabs restored from the recent-files list) gets a Save button that
does nothing.

**Possible fix.**
1. Make the guard explicit: if `!podeSalvar`, disable the Save action (`btnSalvar.setEnabled(false)`)
   or route the action to the **Save As** dialog instead of returning silently.
2. Audit every construction path of `AbaCodigoFonte` (`novaAba`, `setCodigoFonte`,
   `AbaCodigoFonteRecuperavel`, recent-files restore) and add an assertion/log when `podeSalvar`
   stays `false` on an editable document.
3. Add a status-bar confirmation ("Arquivo salvo — `C:\…\programa.por`") so the user can tell the
   difference between "saved" and "did nothing".
4. For #989 (names with digits) verify the `getArquivoComExtensao()` / `FileHandle` path assembly;
   *hypothesis:* the recently-used list stores the name without extension and the reopen path used
   by the restore logic then rejects it.

**Acceptance criteria.** No code path can leave an editable tab with a Save button that silently
does nothing; every save attempt produces either a written file or a visible message.

---

### PS-004 · Recovered-temporary-files dialog floods the IDE on every start
`P1` · `MED` · **Labels:** none · **Status:** open · **Issues:** #1201 · #1114 *(and #1113, the crash during the recovery autosave itself — see PS-002)*

**Details.** *"O PROGRAMA VIVE DANDO O ERRO DE ARQUIVOS TEMPORARIOS RECUPERADOS AO ABRIR TODAS AS
VEZES… ABRE VÁRIAS JANELAS DE DOCUMENTOS JÁ ABERTOS ANTERIORES E TENHO QUE FECHAR UMA POR UMA."*
The user reinstalled and even reformatted the machine; the flood returns. The only reported
workaround is formatting the PC, which means the temp folder is never cleaned or is being
re-populated faster than it is cleared.

**Root cause (*hypothesis*).** `AbaCodigoFonte.salvaArquivoRecuperavel()` (`:933-978`) writes one
`<name>_recuperado.por` file per **keystroke-triggered autosave**, toggling the name between `_2`
and no suffix through `isPrimeiroRecuperavel`. `salvaArquivoRecuperavel()` runs from
`PortugolParser.parse` (`:196`) and from the tab-changed listener. Startup restores **every** file
found in `Configuracoes.getDiretorioTemporario()` and shows one dialog per file. On a machine where
the IDE was killed (or crashed — see PS-001) the folder accumulates dozens of stale files.

**Possible fix.**
1. Keep a single recovery file per logical document (key by the original absolute path, hash it),
   and rewrite it in place instead of toggling names.
2. On start-up, group recoveries: one dialog listing *all* recoverable documents with checkboxes,
   defaults to "restore nothing older than N days".
3. Delete a recovery file when the corresponding document is saved successfully or explicitly
   discarded; add a "limpar arquivos de recuperação" button in the preferences.
4. Cap the number of restorable documents (e.g. 10) and log the rest instead of opening them.

**Acceptance criteria.** Killing the IDE mid-edit N times produces at most one recovery entry per
document, and start-up shows one consolidated dialog.

---

### PS-005 · `portugol-console -no-wait` exits with code 0 on runtime errors and prints no message
`P0` · `MIN` · **Labels:** none (filed by an external consumer) · **Status:** open · **Issue:** #1218

**Details.** Reported by the [microHelium](https://github.com/UnitedOpen-Source/microHelium)
automatic judge, which runs `portugol-console` unattended to grade student submissions. With
`-no-wait`, a program that divides by zero exits with status **0** and the
`Erro de execução: …` line is not printed at all, so the judge marks wrong answers as correct.
Compilation errors still print to `stderr`, but the exit code is still 0.

**Root cause (*verified*).**
In `console/src/main/java/br/univali/portugol/Console.java`,
`execucaoEncerrada(…)` switches on `getModoEncerramento()` and prints the `ERRO` message **only
inside `if (aguardarParaSair)`** (line 526-531), then unconditionally calls
`System.exit(CodigoEncerramento.NORMAL.ordinal())` (line 535). So with `-no-wait` (`aguardarParaSair
== false`) the message is skipped *and* the exit code is forced to `NORMAL`. `extrairParametroAguardarParaSair`
(line 213) removes the flag and returns `false`, confirming the intent was "don't pause", not
"don't report".

**Possible fix.** Introduce a local `CodigoEncerramento codigo = CodigoEncerramento.NORMAL;`,
set it to `ERRO` in the `ERRO` branch (and to `INTERRUPCAO` where applicable), always print the
error message to `stderr` regardless of `aguardarParaSair`, and finish with
`System.exit(codigo.ordinal())`. Keep `aguardar(...)` responsible only for the ENTER prompt.

**Acceptance criteria.** `portugol-console -no-wait divzero.por` prints
`Erro de execução: …` to `stderr` and exits non-zero; a successful program still exits 0.

---

### B · Platform & distribution

### PS-010 · macOS: `.dmg` reported as corrupted, Catalina+ hides files, crashes on Big Sur/Monterey
`P1` · `MAX` · **Labels:** `MacOS Bugs`, `MacOS system support`, `Install Bugs`, `high-priority` · **Status:** open · `has-PR` (#1097)

**Issues:** #1081 · #1187 · #1204 · #876 · #812 · #1126 · #880 · #990 · #940 · #239
*(PR #1097 has been open since 2023-04; #768 is the macOS plugin-install variant, #1150 the zip-extraction one)*

**Details.** macOS is the worst-served platform in the tracker:
* **Install:** *"A imagem de disco não pode ser aberta. A imagem de disco está corrompida"* on
  Monterey 12.6.1 (#1081), Sonoma 14.7.4 (#1187) and macOS 12 (#1204). The `.dmg` has been broken
  **since 2.7.3**; PR #1097 works around it.
* **File access:** since Catalina 10.15.5, macOS TCC blocks the file chooser, so the IDE "cannot
  see" the user's folders (#876, `high-priority`). Opening via *Recent files* still works because
  the path is known — the report documents this precisely.
* **Start-up crashes:** `EXC_BAD_ACCESS` in the bundled JVM on Big Sur (#880, #990) and a
  `NoClassDefFound` at launch on Monterey (#1126).
* **Quit:** the app does not terminate and the user must force the system (#940).
* **Finder integration:** double-clicking a `.por` opens the IDE but not the file (#239, open
  since 2017, `postponed`/`in-progress`).

**Root cause (*mixed*, partly *verified*).** The `.dmg` is built ad-hoc without notarization;
Gatekeeper reports an unverifiable/quarantined disk image as "corrupted". The bundled JRE
(`Contents/Resources/IDE/java/java-mac`) is a Java 8 build that is not compatible with the
Apple-silicon era or with newer macOS releases. TCC is a privacy feature, not a bug — it needs an
`Info.plist` purpose string and an onboarding dialog pointing the user to
*Privacidade → Acesso total ao disco*. The `openFile` Apple Event handler is not implemented, so
the launch argument is ignored.

**Possible fix.**
1. **Distribution (needs a Mac runner):** build with `jpackage` (JDK 17+) to produce a signed,
   notarized `.app`/`.dmg`; add `xcrun notarytool submit --wait` and `stapler staple` to
   `dist.gradle`. Until then, publish the *unsigned zip* + the documented
   `xattr -dr com.apple.quarantine` workaround from PR #1097 and pin it at the top of the download
   page.
2. **TCC:** add `NSDesktopFolderUsageDescription`, `NSDocumentsFolderUsageDescription`,
   `NSDownloadsFolderUsageDescription` to `Info.plist`, and show a first-run dialog that explains
   the *Full Disk Access* setting.
3. **Apple Events / `openFile`:** implement the `com.apple.eawt.Application` `openFile` listener (or
   `handleOpenFiles`) in `Lancador`/`PortugolStudio.iniciarNovaInstancia` so `argv`-passed `.por`
   files are loaded — the same code path fixes #239 on every OS.
4. **Quit (#940):** ensure `PortugolStudio.encerrar()` is called from the `WindowEvent.WINDOW_CLOSING`
   handler rather than only from the menu action; add a watchdog that force-exits after N seconds.

**Acceptance criteria.** A clean macOS 14 machine can download, open, drag to Applications and
launch without a Gatekeeper warning; the file chooser lists the home directory after the user
grants access; double-clicking a `.por` opens that file.

---

### PS-011 · Upgrade from Java 8 to a supported JDK and from WebLAF to a current release
`P1` · `MAX` · **Labels:** `enhancement`, `optimization`, `JAVA system support` · **Status:** open

**Issues:** #891 *(the umbrella)* · #421 · #1064 · #1206 · #984 · #1094 · #840 · #862 ·
#816 · #858 · #1069 *(monitor/window-management symptoms of the same hand-rolled window code)*

**Details.** The IDE still ships **Java 8**. The maintainers' own issue #891 states the reason it
was postponed: conflicts with the WebLAF look-and-feel, which now has releases compatible with
modern JDKs. Everything in the "rendering" family depends on this upgrade:
* `#421` — the UI multiplies/ghosts itself (with a long analysis thread and StackOverflow
  references; labelled `wontfix` + `couldn't reproduce` precisely because it is a Java/driver issue);
* `#1064` — a Dell G15 on Windows 11 shows the same overlay at every start;
* `#1206` — the window duplicates whenever the mouse is dragged over it;
* `#840`, `#862` — `NoClassDefFound` / "não foi possível criar a Máquina Virtual Java" at start-up;
* `#984`, `#1094` — text is unreadably small on 4K/HiDPI displays (Java 8 has no per-monitor DPI
  awareness on Windows);
* `#816`, `#858`, `#1069` — resizing with two monitors hangs the app, the window cannot be dragged to
  a second monitor, and `Win+Arrow` snapping does not align it. These are **not** strictly Java-version
  bugs: `Lancador` draws its own frame (`setUndecorated(true)`) and implements maximize/snap by hand
  with `MouseInfo.getPointerInfo().getDevice()`, so it fights the window manager. They are grouped here
  because the same rewrite that makes the IDE run on JDK 17 should delete that hand-rolled code —
  see the fix steps below.

**Root cause (*verified by the maintainers' own analysis*).** JDK 8's `D3D`/`XRender` pipelines
plus WebLAF's custom `paintComponent` produce repaint artefacts on certain GPU/driver
combinations. Java 9+ fixed the pipeline, but the repo pins Java 8 in `build.gradle` and bundles
a JRE 8 in `dist.gradle`.

**Possible fix.**
1. Bump the toolchain to JDK 17 (LTS) or 21 in `build.gradle`; run `./gradlew :compilador:test`
   and the IDE smoke tests; fix removed-API usages (`javax.xml.bind`, `sun.*`, `com.apple.eawt`).
2. Update WebLAF to its latest 5.x/JavaFX-free release (`com.github.mgarin:weblaf`) — or, if that
   proves too invasive, migrate the theme layer to FlatLaf, which has first-class HiDPI support.
3. Enable HiDPI: `-Dsun.java2d.uiScale.enabled=true`, `-Dsun.java2d.dpiaware=true` for Windows,
   and a scaled font stack for the tree/tab labels (#984, #1094).
4. Bundle the new JRE in `dist.gradle` and drop the per-OS Java 8 trees.
5. While touching the launcher: replace the hand-rolled maximize/snap math with
   `frame.setExtendedState(...)` + `GraphicsEnvironment.getScreenDevices()`, which also removes the
   `MouseInfo.getPointerInfo()` NPE behind PS-001 (#816, #858, #1069).

**Acceptance criteria.** The IDE runs on JDK 17+, no startup `NoClassDefFound`, the ghosting
reproductions in #421/#1064/#1206 are gone on the machines that reported them, and font scaling
follows the OS setting on a 4K display.

---

### PS-012 · Linux distribution: no Flatpak, no standalone package, no aarch64/ARM build
`P2` · `MED` · **Labels:** `enhancement`, `Linux system support`, `feature` · **Status:** open

**Issues:** #302 *(Flatpak)* · #901 *(standalone 2.7.5 missing)* · #978 *(JAR / armhf / aarch64 / Raspberry Pi)*

**Details.** #302 asks for a Flatpak on Flathub, motivated by immutable distros (Endless OS). #901
points out that the `standalone` Linux package available in 2.7.4 **disappeared** in 2.7.5, which
blocks the Arch AUR maintainer from shipping the new version. #978 asks for a plain JAR or ARM
builds so Raspberry Pi classrooms can use the IDE.

**Possible fix.** Publish a `.flatpak` manifest (`org.freedesktop.Sdk` runtime + the standalone
zip) and submit to Flathub; restore the `standalone` artifact in the release job
(`dist.gradle`) — it is a zip of the already-built tree, so it is nearly free; publish a
platform-independent `portugol-studio.jar` plus a `portugol-console.jar` and test on
`aarch64`/`armhf` (the console module already runs on any JVM with the right natives).

**Acceptance criteria.** The release page lists `standalone`, `jar` and `aarch64` artefacts;
`flatpak install flathub br.univali.portugolstudio` works.

---

### PS-013 · Installer problems: silent install, custom target directory, failing uninstaller
`P2` · `MED` · **Labels:** `Install Bugs`, `feature`, `enhancement` · **Status:** open

**Issues:** #1077 *(silent install for labs)* · #1025 *(install to `/opt`, not `/usr/local`)* · #825 *(uninstaller "Unknown error while running … desinstalar.exe -- mode unattended")* · #1168 *(cannot download/install latest at all — 8 screenshots of errors)*
Also in this group, from the auto-reports: #768 *(plugin install on macOS: `Plugin-Exemplo./ExemploPlugin.jar (No such file or directory)`)*, #925 *(NPE installing a plugin from a bad/missing `.psa`)*, #1150 *(`FileNotFoundException` in `UnzipUtility.unzip`)* — all three are the same "installer/unzipper assumes a well-formed archive" defect.

**Details.** Schools need `./portugol-studio.run --silent` to provision a lab (#1077) and to choose
`/opt` (#1025). #825 shows the uninstaller aborting with a generic BitRock message, which leaves a
half-installed product that then blocks reinstall. #1168 is a user-facing wall of installer errors
on the current download.

**Possible fix.** The installer is generated by `installer/` (BitRock/InstallBuilder): add
`--mode unattended` + `--prefix` pass-through and document them; make the uninstaller tolerate a
missing `desinstalar.exe` (the Java-based uninstaller registry key is stale after a manual delete);
add a "verify download" step with a published SHA-256 on the download page and in `CHANGELOG.md`.

**Acceptance criteria.** `sudo ./portugol-studio.run --mode unattended --prefix /opt/portugol`
installs and uninstalls cleanly in a container.

---

### PS-014 · CRLF line endings break the Linux post-install scripts
`P3` · `MIN` · **Labels:** `bug`, `postponed`, `Linux Bugs` · **Status:** open · **Issue:** #532

**Details.** Shell scripts shipped inside the Linux package contain CRLF because they were committed
from Windows; Linux reports "arquivo não existe" for a file that exists. Documented workaround:
`dos2unix`. The maintainers' stopgap was to make the installer ignore the exit status, so the error
is hidden rather than fixed.

**Possible fix.** Add `.gitattributes` rules (`*.sh text eol=lf`) — `.gitattributes` already exists,
so extend it — and add a normalisation step (`find . -name '*.sh' -exec sed -i 's/\r$//' {} +`) to
the Linux packaging task in `dist.gradle`. Then re-enable the strict exit-status check in the
installer.

**Acceptance criteria.** `file` reports CRLF-free scripts inside the produced package; the
installer no longer needs to ignore exit codes.

---

### PS-015 · Linux: hangs, freeze on start-up and "must reboot to reopen"
`P2` · `MED` · **Labels:** `Linux Bugs` · **Status:** `needs-repro`

**Issues:** #1022 *(freezes on Zorin)* · #1074 *(hangs and locks the whole PC)* · #1178 *(Mint: will not reopen until reboot)* · #1209 *(fullscreen console output scrolls too fast to read)*

**Details.** Four independent Linux reports with thin reproduction data. #1178 is the most
interesting: after closing the IDE it cannot be started again until the machine reboots, which
suggests a **stale single-instance lock / socket** rather than a crash. #1209 is a usability bug —
the console flushes the whole output and the final result is unreadable.

**Possible fix.** *Hypothesis for #1178:* the `Info.ps` lock file or the local socket used by
`PortugolStudio`'s "new instance → existing instance" handshake is not removed on
`SIGKILL`/window-manager close; add a PID-liveness check plus a timeout when connecting, and delete
the stale lock. For #1209, add an "auto-scroll / pause on finish" option and a configurable delay
before the console is cleared.

**Acceptance criteria.** Closing the IDE with `SIGKILL` and restarting it works immediately; the
final console output stays on screen.

---

### C · Core language: lexer, parser, semantic analysis, code generation

### PS-020 · `escolha` rejects `cadeia`, but the built-in help documents `cadeia`
`P1` · `MED` · **Labels:** `Analise Sintática/Semântica`, `Ajuda Error` · **Status:** open

**Issues:** #1153 *(desktop)* · #1120 *(help page says it works)* · #1137 *(Android vs desktop divergence)*

**Details.** `escolha (nome)` with a `cadeia` works in Portugol WebStudio but fails on the desktop
with *"O comando 'escolha' espera uma expressão do tipo 'inteiro' ou 'caracter'"*. The built-in help
lesson for `escolha-caso` **shows an example using `cadeia`**, so the IDE contradicts itself. #1137
reports the same class of Android-vs-desktop divergence.

**Root cause (*verified*).** `AnalisadorSemantico.visitar(NoEscolha)`
(`core/src/main/java/br/univali/portugol/nucleo/analise/semantica/AnalisadorSemantico.java:1406-1428`)
explicitly rejects everything except `INTEIRO` and `CARACTER`. The Java code generator emits a plain
`switch`, which is why strings were excluded.

**Possible fix.** Either
(a) *support it* — accept `TipoDado.CADEIA` in the semantic check and, in
`GeradorCodigoJava`, lower a `cadeia` `escolha` to an `if/else if` chain comparing with
`.equals(...)` (or `switch` on `hashCode()` with an equals guard); or
(b) *document it* — change the help example in the `escolha` lesson to `inteiro`/`caracter` and add
an explicit note. **(a) is the better fix** because WebStudio already behaves that way and students
move between the two; pick (b) only as a stopgap for the next release.

**Acceptance criteria.** The snippet from #1120 runs identically on desktop and WebStudio, or the
help page no longer advertises `cadeia`.

---

### PS-021 · Vector/matrix size must be a literal or a constant — variables rejected
`P1` · `MAX` · **Labels:** `enhancement` · **Status:** open · **has-PR (#1220)**

**Issues:** #911 · #1198 · #1121 · #1190 · #1182 · #1161

**Details.** `leia(n)` then `inteiro v[n]` is the single most requested language feature:
* #911 asks for it explicitly ("Isso é um comportamento muito comum em muitas linguagens");
* #1198 and #1190 hit it in classroom exercises;
* #1121 reports that even a **`const inteiro`** is rejected by Portugol *WebStudio* (a parity bug —
  it works on the desktop);
* #1182 (identity-matrix exercise) and #1161 (battle-ship matrix) are teacher-reported cases where
  the desktop rejects or mis-executes code that works on the web.

**Root cause (*verified*).** `AnalisadorSemantico.obterTamanhoVetorMatriz(…)`
(`…/AnalisadorSemantico.java:2505-2580`) accepts only `NoInteiro`, a `const` variable whose
initialiser is a literal, a library constant, or a limited `NoOperacao` handled by
`AnalisadorDeclaracaoTamanhoVetorMatriz`. A plain variable falls into the `else` branch and reports
`ErroTamanhoVetorMatriz` ("deve ser um valor ou uma constante do tipo inteiro e positivo"). The
ASA/`Vetor` symbol stores a fixed `tamanho` at compile time, and the Java generator emits
`new int[n]`, so supporting a runtime size needs a real design decision (the AST must carry the
size *expression*).

**Possible fix.** PR [#1220](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/1220) already
implements this for local scope with a runtime check; the review work is:
1. confirm the generated Java is `new int[<expr>]` guarded by a `se (expr <= 0) { erro }` check;
2. extend the same allowance to **matrices** and to the inspector (the inspector needs the runtime
   size — see PS-051);
3. decide the semantics for the global scope (reject with a clear message, as the PR does);
4. add regression tests for `n = 0`, negative `n`, and a size expression that throws.
Also fix #1121 on the WebStudio side (const-integer rejection) so all three front-ends agree.

**Acceptance criteria.** `inteiro v[n]` inside `inicio()` works on desktop, web and Android with a
clear runtime error for `n <= 0`; the three issues close together.

---

### PS-022 · A function cannot declare a vector/matrix return type
`P2` · `MAX` · **Labels:** `feature` · **Status:** open · **Issue:** #1162

**Details.** There is no way to write a function that builds and returns a `vetor`/`matriz`; the
workaround is a global variable that the function fills — which works on the web build but not on
the desktop, per the report.

**Root cause (*verified in the grammar*).** `core/src/main/antlr/Portugol.g4` allows
`TIPO` + `QUANTIFICADOR` only for declarations of variables/parameters, not for the return type of
`NoDeclaracaoFuncao`; `TipoDado` has no array variant, so `Vetor`/`Matriz` cannot be a return type.

**Possible fix.** Add `VETOR`/`MATRIZ` quantifiers to the function return type in the grammar, carry
them in `NoDeclaracaoFuncao`/`Funcao`, and emit `int[]`/`int[][]` in `GeradorCodigoJava`; the
inspector and `TipoDado` comparisons need the quantifier too. This is a language change — it needs
an ADR/discussion issue before implementation (mirror it in WebStudio, which already supports the
global-variable variant).

**Acceptance criteria.** `funcao inteiro[] monta()` compiles and its result can be assigned and
indexed; a spec is agreed with the WebStudio maintainers first.

---

### PS-023 · `sorteia(min, max)` inside a vector initialiser throws `NullPointerException`
`P1` · `MIN` · **Labels:** `bug`, `Biblioteca Bugs`, `New functions` · **Status:** open

**Issues:** #632 · #1037 · #606

**Details.** `inteiro x[2] = { sorteia(0,10), 0 }` fails; moving the call out of the braces, or
using `u.sorteia(...)` from the `Util` library, works. The runtime exception surfaces as
`java.util.concurrent.ExecutionException: java.lang.NullPointerException` from
`AbaCodigoFonte$AcaoExecucao`. #606 separately argues that the library-level `sorteia` became
redundant once the language got its own, which is exactly the duplicate that hides this bug.

**Root cause (*hypothesis, consistent with the frames*).** The language-level `sorteia` is
implemented in `Programa.java:908` inside the generated program; a call used as an **initialiser
element** is evaluated before the library/`Random` instance for that program is attached, so the
call dereferences `null`.

**Possible fix.** In `PreCompilador`/`GeradorCodigoJava`, hoist initialiser-list expressions into
the statement list that runs *after* the program's built-in library context is initialised (or
initialise `Random` lazily inside `Programa.sorteia`). Deprecate the `Util.sorteia` duplicate with a
documented alias so both spellings keep working.

**Acceptance criteria.** The snippet in #632 runs and the two spellings behave identically.

---

### PS-024 · Escape sequences do not work in `caracter` literals (`'\n'`, `'\t'`, `'\''`)
`P1` · `MIN` · **Labels:** `bug` · **Status:** open · **Issue:** #1049

**Details.** `caracter quebra = '\n'` yields a literal backslash; `'\''` and `'\t'` behave the same
way. The only workaround is extracting the character from a `cadeia` with the `Texto` library.

**Root cause (*verified*).** `GeradorASA` builds the node with
`new NoCaracter(ctx.CARACTER().getText().charAt(1))`
(`core/src/main/java/br/univali/portugol/nucleo/analise/sintatica/GeradorASA.java:895`) — it takes
the character at index 1 and stops, so `'\n'` becomes `\`. The `cadeia` path, a few lines above
(`:879`), correctly calls `StringUtils.traduzirSequenciasEscape(substring)`.

**Possible fix.** Pass the token interior through
`StringUtils.traduzirSequenciasEscape(texto.substring(1, texto.length() - 1))` and require the
result to be exactly one character; otherwise report a proper syntax error
(`ErroCaracterInvalido`) instead of silently truncating.

**Acceptance criteria.** The three examples in #1049 print `'`, a newline and a tab; `'ab'` reports a
readable error instead of taking `a`.

---

### PS-025 · Concatenating a `logico` writes `true`/`false` instead of `verdadeiro`/`falso`
`P2` · `MIN` · **Labels:** `bug` · **Status:** open · **Issue:** #1050

**Details.** `escreva("O valor é: " + verdadeiro)` prints `…true`, while `escreva(", ", verdadeiro)`
correctly prints `verdadeiro`. Two spellings of the same sentence give different output — a nasty
surprise in a teaching language.

**Root cause (*verified*).** `GeradorCodigoJava.visitar(NoLogico)` emits the Java literal
(`String valor = noLogico.getValor() ? "true" : "false";`,
`core/…/execucao/gerador/GeradorCodigoJava.java:352`), and Java's string concatenation then
stringifies it.

**Possible fix.** In the concatenation lowering, wrap `logico` operands in
`Biblioteca.<boolToString>(x)` (a helper returning `"verdadeiro"`/`"falso"`), or give `NoLogico`
a `toString()` in the generated class. Keep the Java literal for boolean contexts.

**Acceptance criteria.** Both `escreva` forms in #1050 print `verdadeiro`.

---

### PS-026 · `'A' + 'B'` is type-checked as `cadeia` but evaluated as an integer
`P2` · `MED` · **Labels:** `bug` · **Status:** open · **Issue:** #1048

**Details.** `escreva('A'+'B')` prints `131` (char-code addition), but `inteiro junto = 'A'+'B'`
is rejected with *"a variável deveria ser do tipo cadeia"*, and `cadeia junto = 'A'+'B'` produces a
Java-level compilation error. Three different type decisions for one expression.

**Root cause (*verified in behaviour, hypothesis in code*).** The semantic analyzer types the sum of
two `caracter` as `CADEIA` (mirroring Java's `char + char → int` mismatch), while the generator emits
`char + char`, which Java evaluates as `int`.

**Possible fix.** Pick one rule and apply it in all three places: the simplest consistent choice is
*`caracter + caracter → inteiro`* (document it, since it is what Java does and what the runtime
already produces), then update `AnalisadorSemantico`'s typing of `NoOperacaoSoma` and the help entry
for arithmetic operators.

**Acceptance criteria.** The three snippets in #1048 either all compile and print `131`, or all fail
with a single consistent message.

---

### PS-027 · `ClassCastException` / `NullPointerException` when a vector or matrix reference is used as an argument
`P1` · `MED` · **Labels:** `bug`, `Analise Sintática/Semântica` · **Status:** open

**Issues:** #929 · #1205 · #1145 · #1085

**Details.** Four crash reports with the same flavour:
* #929 — `NoReferenciaVetor cannot be cast to NoReferenciaVariavel`
  (`PreCompilador.visitar(PreCompilador.java:128)`);
* #1205 — `NoReferenciaMatriz cannot be cast to NoReferenciaVariavel` (same frame);
* #1145 / #1085 — `NullPointerException` in `NoMatriz.getTipoResultante(NoMatriz.java:72)` from
  `GeradorDeclaracaoVariavel.gera:31`.

All of them happen when a `vetor`/`matriz` element (or a whole vector) is **passed to a function**
or **used to initialise a variable**. The user sees a raw stack trace instead of either working code
or a semantic error.

**Root cause (*verified*).** `PreCompilador` assumes every argument/reference is a
`NoReferenciaVariavel` and casts unconditionally at line 128; `NoMatriz.getTipoResultante` assumes
`getInicializacao()` is non-null.

**Possible fix.** Replace the casts with `instanceof` dispatch (handle `NoReferenciaVetor`,
`NoReferenciaMatriz`, `NoReferenciaVariavel` and expression nodes), and null-guard
`NoMatriz.getTipoResultante` so an untyped matrix reports
`ExcecaoImpossivelDeterminarTipoDado` instead of an NPE. Then re-check the generator's variable
declaration path (`GeradorDeclaracaoVariavel.gera:31`) for the matrix initialisation case.

**Acceptance criteria.** The snippets from #929/#1205/#1145 compile correctly (or report a
semantic error); no `ClassCastException`/`NullPointerException` can escape to the user.

---

### PS-028 · Parser: `^=` inside `para`, and `}` inside strings/comments
`P1` · `MED` · **Labels:** `bug`, `Analise Sintática/Semântica` · **Status:** open

**Issues:** #893 · #924 · #1160

**Details.**
* #893 — `a ^= b` inside a `para` block makes the compiler claim *"O comando para necessita de ao
  menos uma condição de parada"*, although the condition is present and the equivalent
  `a = a ^ b` compiles.
* #924 — a closing brace `}` inside a `cadeia`, `caracter` or a comment breaks the parse.
* #1160 — a syntactically valid `escreva(...)` is flagged as missing its opening parenthesis
  while an identical line below is accepted (looks position-dependent, possibly after an
  incremental re-parse).

All three point at the ANTLR grammar (`core/src/main/antlr/Portugol.g4`,
`PortugolLexico.g4`) losing track of token boundaries after an unusual token.

**Root cause (*hypothesis*).** In #893 the `^=` token is likely consumed by a rule that also eats the
statement terminator, which ends the `para` clause early; in #924 the `}` inside a string should be
protected by the lexer's string mode but is probably re-lexed by the error-recovery path.

**Possible fix.** Add `^=` (and `&=`, `|=`, `<<=`, `>>=`) to the compound-assignment rule *after*
the plain `^` token, verify the `para` rule cannot consume a `;` produced inside a compound
assignment; ensure `CADEIA`/`COMENTARIO` lexer rules precede the punctuation rules and are never
split by error recovery. Add the three snippets as `.por` regression fixtures (see PS-061).

**Acceptance criteria.** All three snippets compile without diagnostics.

---

### PS-029 · `vetor[i]++` followed by `escreva` produces a semantic error
`P2` · `MED` · **Labels:** `bug`, `Analise Sintática/Semântica` · **Status:** open · **Issues:** #899 · #680

**Details.** #899: `cod[0]++; func(); a++; escreva(0)` is rejected, although reordering or using
`cod[0] = cod[0] + 1` works. #680 is the related numeric-expression bug: any unary operator
(`+ - ++ -- ~ !`) in a compound expression fails to compile without extra parentheses
(`v = -v++`, `v = - -1 + 1`).

**Root cause (*hypothesis*).** The post/pre-increment lowering and the unary-operator precedence in
`GeradorASA`/`FabricaNoOperacao` interact badly: the increment is emitted as an expression whose
type is `vazio`/unknown, so the following statement inherits a broken type.

**Possible fix.** Lower `x++`/`x--` (statement form) to an explicit assignment node during semantic
analysis rather than in the generator, and fix the unary precedence in the grammar so
`-v++` parses as `-(v++)`. Cover both with the fixtures added in PS-061.

**Acceptance criteria.** The #899 snippet compiles; the #680 program prints the values promised in
its own comments.

---

### PS-030 · `inicio()` accepts a return type; starting the graphics mode twice throws NPE
`P2` · `MIN` · **Labels:** `bug`, `ANTLR Error`, `Biblioteca Bugs` · **Status:** open · **Issues:** #920 · #918 · #922

**Details.** #920 — declaring `funcao inicio(): inteiro` is accepted by the syntax/semantic analysis
and only fails at run time. #918 — calling `iniciar_modo_grafico()` twice throws a `null` error
(with an `escreva` in between it reports "interrompido"). #922 — running the `PortugolStudio`
library itself errors out.

**Possible fix.** Reject a non-`vazio` return type on `inicio` in `AnalisadorSemantico` (a
`ErroFuncaoInicioComRetorno` message), and make `Graficos.iniciar_modo_grafico` idempotent: if the
window already exists, reuse it (or raise a clear `ErroExecucaoBiblioteca`) instead of
dereferencing `null`.

**Acceptance criteria.** `inicio(): inteiro` is a compile-time error with a readable message; a double
`iniciar_modo_grafico()` keeps working.

---

### PS-031 · Integer overflow is silent, and `-2147483648` is rejected although it is advertised
`P2` · `MIN` · **Labels:** `bug`, `Bad Error message` · **Status:** open · **Issues:** #1172 · #921 · #1090

**Details.** #1172 — `2^70` returns `0`, `1` or the base, "totalmente aleatório"; the tutorial never
states the maximum integer. #921 — the IDE says the valid range is `-2147483648 … 2147483647` but
refuses to accept `-2147483648` (it is parsed as unary minus applied to an out-of-range literal).
#1090 — "broken numbers not entering the right concept" (real vs integer display).

**Possible fix.** In the lexer/`AnalisadorStringInvalida`, special-case the `-2147483648` literal
(parse the magnitude as a `long` before negation); detect `Math.multiplyExact`/`addExact` overflow in
the numeric operators and raise `ErroExecucaoBiblioteca("Estouro de inteiro")`; document the range in
the `inteiro` help page and in the arithmetic-lesson text.

**Acceptance criteria.** `-2147483648` is accepted, `2^70` raises a clear overflow error, and the
help states the limits.

---

### PS-032 · Repeated compilation fails: `ClassFormatError`, "Erro na compilação" for valid code
`P2` · `MED` · **Labels:** `bug`, `Gerador de JAVA` · **Status:** `needs-repro` · **Issues:** #1091 · #1017 · #1052

**Details.** #1091/#1017 — `java.lang.ClassFormatError` raised from
`Compilador.carregaProgramaCompilado(Compilador.java:309)`: the class generated by the in-memory
compiler is rejected by the JVM. #1052 — *"Todo e qualquer código que antes era compilado agora
ocorre mensagem de erro"*, i.e. the whole IDE is stuck in a bad state.

**Root cause (*hypothesis*).** The generated class name is derived from a hash/time; a cached
`ClassLoader` or `Programa` instance from a previous compilation is reused after the class was
evicted (or the temp `.class` was truncated when two compilations raced).

**Possible fix.** Use a fresh, uniquely named `ClassLoader` per compilation; write the `.class` with
an atomic move; clear the compilation cache when the source changes; log the generated Java source
for `ClassFormatError` to make the next report actionable.

**Acceptance criteria.** Compiling the same program ten times in a row always succeeds.

---

### PS-033 · Two `NullPointerException`s in the compiler pipeline
`P2` · `MIN` · **Labels:** `bug` · **Status:** `needs-repro` · **Issues:** #1007 · #1202

**Details.** #1007 — NPE in
`br.univali.portugol.nucleo.execucao.gerador.helpers.Utils.getNomeTipoJava(Utils.java:282)`, i.e. a
type could not be mapped to a Java type name. #1202 — NPE in
`BuscadorDeSimbolo.visitar(BuscadorDeSimbolo.java:603)` while resolving a `NoReferenciaVariavel`.

**Possible fix.** Both are missing `null` branches: `getNomeTipoJava` should throw a
`ExcecaoImpossivelDeterminarTipoDado` (which the IDE can translate) instead of NPEing, and
`BuscadorDeSimbolo` should treat an unresolved symbol as "not found" and report
`ErroSimboloNaoDeclarado`. No user-visible behaviour change beyond a readable message.

**Acceptance criteria.** Neither stack frame can produce an NPE; a semantic error is reported instead.

---

### D · Runtime libraries (Gráficos, Util, Texto, Tipos, objetos)

### PS-040 · 3-digit hexadecimal colours are expanded to the wrong value
`P2` · `MIN` · **Labels:** `bug`, `Biblioteca Bugs` · **Status:** open · **Issues:** #915 · #1049 *(see PS-024 for the escape half)*

**Details.** `#456` should expand to `#445566` (each digit doubled); Portugol produces `#000456`,
i.e. it zero-pads on the left. Any three-digit colour is therefore wrong.

**Root cause (*verified in behaviour*).** The colour string is converted by
`Integer.parseInt(cor.substring(1), 16)` and then fed to `new Color(int)` /
`new Color(vermelho, verde, azul)` (`core/…/bibliotecas/Graficos.java:1436`, `:880`), which
interprets the value as a full 24-bit RGB number. Three digits give a tiny number, hence the
"leading zeros".

**Possible fix.** Add a normaliser: if the hex body has length 3, expand each nibble
(`String.valueOf(c).repeat(2)`) *before* parsing; reject lengths other than 3 or 6 with
`ErroExecucaoBiblioteca` and a message that names both accepted forms.

**Acceptance criteria.** `#456` yields exactly `#445566`; an invalid colour raises a readable error.

---

### PS-041 · `manter_visivel = falso` still forces the window on top
`P2` · `MIN` · **Labels:** `bug`, `Biblioteca Bugs` · **Status:** open · **Issues:** #917 · #951 *(related literal-handling bug, see PS-043)*

**Details.** With `iniciar_modo_grafico(falso)` the window still stays above other applications,
which the parameter is supposed to prevent.

**Root cause (*verified*).** `JanelaGraficaImpl.exibir(boolean manterVisivel)` calls
`setAlwaysOnTop(manterVisivel)` once at show time
(`core/…/bibliotecas/graficos/JanelaGraficaImpl.java:121-128`), but the window is
`setUndecorated`/`alwaysOnTop` re-applied elsewhere (the fullscreen path), so the flag is lost after
the first repaint or focus change.

**Possible fix.** Store the flag on the `JanelaGraficaImpl` instance and apply it in every
`show`/`toFront`/focus-gain path; test with a second window on top.

**Acceptance criteria.** With `falso`, another application can cover the graphics window; with
`verdadeiro`, it stays on top.

---

### PS-042 · Object library corrupts addresses when objects are freed
`P1` · `MIN` · **Labels:** `bug` · **Status:** open · `has-PR` (#995) · **Issue:** #994

**Details.** `remove(int indice)` on the internal `ArrayList` shifts every later element left, so
addresses still held by Portugol code now point to a different (or missing) object. This is a
use-after-free of the teaching "pointers" — a correctness bug in a library students rely on to
learn references. The reporter states the fix exists in **PR #995**.

**Possible fix.** Do not compact the list on free: keep a free-list/`null` slot, or store objects in a
`HashMap<Integer, Object>` keyed by the address so freeing never renumbers the survivors.

**Acceptance criteria.** Freeing object *i* leaves every other address valid; a regression test that
frees middle, first and last elements passes.

---

### PS-043 · `escreva` breaks on non-UTF-8 consoles and mixes `\n` handling
`P1` · `MED` · **Labels:** `bug`, `Gerador de JAVA` · **Status:** open · **Issues:** #951 · #1004 · #1065

**Details.** #1004 — accented text prints as `ﾃｧﾃ` tofu boxes (console encoding). #951 — string
literals inside a **vector initialiser** silently drop `\n`, `\"` and `\\` (they work in a plain
`escreva`). #1065 — after saving and reopening, the text in one file becomes "Japanese" characters,
i.e. the file was read back with the wrong charset.

**Root cause (*mixed*).** #951 is the `GeradorASA` string-literal path for `NoVetor` elements not
calling `StringUtils.traduzirSequenciasEscape` (the same helper used at `GeradorASA.java:879`).
#1004/#1065 are encoding issues: the console is created without an explicit `UTF-8` charset and the
file reader/writer does not force UTF-8 (relying on the platform default, which is `windows-1252` on
pt-BR Windows).

**Possible fix.** Route every literal — including vector/matrix initialisers — through
`traduzirSequenciasEscape`; construct the console `PrintStream` with `StandardCharsets.UTF_8` and set
`file.encoding`/`sun.stdout.encoding` in the launcher; force `UTF-8` in `FileHandle.read/save`
(`ide/src/main/java/br/univali/ps/ui/utils/FileHandle.java`) and migrate old files by detecting the
BOM/validity.

**Acceptance criteria.** The `\n` inside a vector works, accented output is correct on a pt-BR
Windows console, and a file saved with accents reopens intact.

---

### PS-044 · Library-level issues: `sorteia` duplication, `Util`/`Arquivos` organisation, `.` completion
`P3` · `MIN` · **Labels:** `bug`, `enhancement`, `New functions`, `Biblioteca Bugs`, `good first issue` · **Status:** open

**Issues:** #606 · #601 · #1030 · #848 · #913 · #716 · #841 · #842 · #843 · #923 · #1102 · #1112

**Details.** A cluster of small, self-contained library requests and defects:
* #606 — `Util.sorteia` is now redundant with the language function (see PS-023) — deprecate or alias.
* #601 — move `obter_diretorio_usuario()` from `Util` to `Arquivos` (**good first issue**).
* #1030 — typing `u.` does not pop up the library function list.
* #848 — `Matematica.valor_absoluto` requires `real`, refuses `inteiro`.
* #913 — add `Graficos.transladar(x, y)` (HTML-canvas `translate`).
* #716 — a long wish-list of vector/matrix helpers (append, remove, insert).
* #841 — `escrevanl()` (escreva + `\n`) for beginners.
* #842 — `leia` with timeout (a specific classroom use case, quoted from an email).
* #843 — a Scratch-style "repeat N times" loop for younger students.
* #923 — `informe()`/`avise()`/`erro()` console variants with colour/icon.
* #1102 / #1112 — printf-style `%.<n>f` formatting / rounding a real to N decimals.

**Possible fix.** Split into individual `good first issue` tickets; the cheap wins are #601, #841,
#848 (overload `valor_absoluto`), #1102/#1112 (expose a `formata`/`arredonda` helper in `Util`) and
#1030 (the completion popup is a `PortugolParser`/`AutoComplete` mapping problem). #842, #843 and #716
need a language/library design decision and should be discussed, not coded ad hoc.

**Acceptance criteria.** Per sub-item; #601/#841/#848/#1102 are each a single small PR.

---

### PS-045 · Index/selection arithmetic crashes: `IndexOutOfBounds`, NPE in the editor and file chooser
`P2` · `MED` · **Labels:** `bug`, `Editor Bugs`, `RSyntaxTextArea Bugs`, `Inspetor issues` · **Status:** `needs-repro`

**Issues:** #896 · #1073 · #1151 · #1119 · #1045 · #1043 · #991 · #957 · #970 · #1192 · #1159 ·
#1149 · #988 · #972 · #1155 · #959 · #1032 · #1066 · #1170 · #1210

**Details.** A family of crashes produced by the editor and the file chooser while the user is simply
typing, selecting or picking a file:
* #896 — `java.util.Vector.elementAt` → `ArrayIndexOutOfBoundsException: 1 >= 1` from the
  file-chooser path (recent-files list racing the dialog).
* #1073 / #970 — NPE in `Editor.selecionarTexto(Editor.java:1165)`.
* #1192 / #1159 / #1149 / #988 / #972 — `ArrayIndexOutOfBoundsException` in
  `PortugolParser.adicionarErroAviso(PortugolParser.java:257)` — the parser reports an error whose
  source range lies outside the current document (stale offsets after an incremental re-parse).
* #959 — NPE in `Editor.highlightLinha(Editor.java:1441)`.
* #1066 / #1032 — `BadLocationException` in `Editor.posicionarCursor(Editor.java:1530)`, called from
  `exibirMensagemCompilador` — the compiler points at a line that no longer exists.
* #1170 — NPE in `org.fife.ui.rsyntaxtextarea.ParserManager.noticeContainsPosition` (a tooltip is
  requested for a position the parser no longer knows).
* #1210 — `IndexOutOfBoundsException: Invalid index` while the save dialog is open.
* #1155 / #1045 / #1043 / #991 / #1151 / #1119 — bare NPE / AIOOBE from the global
  `TratadorExcecoes.uncaughtException` with no application frame, i.e. thrown on the EDT from a
  callback that does not guard its inputs.

**Possible fix.** Validate the `trechoCodigoFonte` offsets against `document.getLength()` before
building a `Position`/`offset` (`adicionarErroAviso`), and clamp instead of throwing. Guard
`selecionarTexto`/`highlightLinha` with a range check. Make the recent-files list thread-confined
(read it only on the EDT) and use `isEmpty()` before `elementAt`. Additionally, stop letting
`TratadorExcecoes` show raw AIOOBE/NPE to end users: those are internal-bug dialogs and should offer
"copiar detalhes" (see PS-060).

**Acceptance criteria.** Typing/selecting rapidly in a large file never produces these stack traces;
an out-of-range diagnostic is dropped with a log line instead of crashing.

---

### E · Editor, inspector, console & UI

### PS-050 · Console tab stays on "Mensagens" and cannot be switched — program output invisible
`P0` · `MED` · **Labels:** `bug`, `Interface bugs`, `Inspetor issues` · **Status:** open

**Issues:** #1100 · #1060 · #1177 · #599 · #1179 · #1158

**Details.** A large, very damaging family: after opening a saved file or a second tab, the bottom
panel stays on the **Mensagens** tab and the Console tab is unclickable, so the program runs but no
output is shown. #1179 — *"o resultado não aparece, … simplesmente não aparece o resultado"* (users
conclude the program is broken). #1158 — *"roda de boa, mas se fechar o Portugol e abrir, não roda
mais"*. #599 and #1060 describe exactly the same thing from 2018 and 2022, and #1177 adds the
workaround: keep creating new tabs until one works.

**Root cause (*hypothesis, high confidence*).** The panel's tab selection is driven by a listener on
"compilation finished / execution started" events; when a document is restored or a tab is created
without a compilation, the selection stays on the Messages tab and the tab's enablement state is
never re-evaluated. #1060 mentions a stray empty message line in the new tab, which suggests the
messages list receives an empty entry and the tab logic then locks.

**Possible fix.** Make the console tab the default and force-select it on `execucaoIniciada`; never
disable the Console tab (only grey the *content*); skip empty message entries in
`PainelTabuladoPrincipal`/`AbaConsole`; re-evaluate the tab enablement when a `AbaCodigoFonte`
becomes the active tab. Add a regression test that opens two files in sequence and asserts the
console is selected after `F9`.

**Acceptance criteria.** Opening any saved file, running it, and seeing output works on the first
attempt, every time.

---

### PS-051 · Variable inspector shows stale/blank values for arrays and matrices
`P1` · `MED` · **Labels:** `bug`, `Inspetor issues` · **Status:** open

**Issues:** #887 · #1051 · #900 · #1056 · #1054 · #279

**Details.** The inspector —
* stops printing values for large vectors (~60+ elements, #887);
* shows wrong values in the second column of a matrix unless stepping through (#1051);
* omits a vector position that was filled via `leia` (#900);
* is completely empty on some Linux setups — variables cannot be dragged in at all (#1056);
* NPEs in `InspetorDeSimbolos.adicionaNoVetor(InspetorDeSimbolos.java:662)` (#1054).

#279 is the design discussion (scoped variables with the same name should appear/disappear as the
debugger enters/leaves a function).

**Root cause (*hypothesis*).** The inspector rebuilds from `Inspecionar` events that are throttled for
large arrays, and it keys rows by name only (so a matrix row is overwritten by the next column). The
empty-inspector case (#1056) looks like a failed drag-and-drop `TransferHandler` on that desktop
environment rather than a data problem.

**Possible fix.** Key rows by *scope + name + indices*; page or lazily expand large arrays instead of
truncating them; refresh the matrix cells on every `Sincronizar` event rather than on selection;
null-guard `adicionaNoVetor`; make drag-and-drop fall back to a "adicionar variável" menu so #1056 has
a workaround.

**Acceptance criteria.** The three snippets in #887/#1051/#900 display correct values while running
(not only while stepping).

---

### PS-052 · Dialog/notification rendering: ghosting, duplicated UI, tiny fonts, monitor handling
`P2` · `MED` · **Labels:** `bug`, `Interface bugs` · **Status:** open

**Issues:** #1184 · #1105 *(and, as *related symptoms*, the items listed under PS-011)*

**Details.** #1184 — the mouse appears to "click by itself": as the pointer passes over the window the
UI reacts as if it had been clicked. #1105 — the graphics window blinks continuously in fullscreen
mode until the user presses `Alt+Tab` and returns. Both are repaint/focus artefacts of the custom-drawn
frame.

**Root cause (*hypothesis*).** The IDE uses an undecorated frame with its own resize/maximize/focus
handling (`Lancador`, `BorderPanel`); focus-restore paths re-fire mouse events, and the fullscreen
graphics window re-triggers `alwaysOnTop` on each repaint (see PS-041 for the same code path).

**Possible fix.** Do **PS-011** first (it likely removes both). Then, in the frame/border code, guard
the synthetic mouse handling against re-entrant focus events, and make the fullscreen graphics window
apply `alwaysOnTop`/fullscreen state only on a real state change, not on every paint.

**Acceptance criteria.** Moving the mouse across the window never triggers a click; fullscreen
graphics mode does not blink.

---

### PS-053 · New documents open with the "Olá Mundo" example instead of an empty file
`P1` · `MIN` · **Labels:** `bug` · **Status:** open · **Issues:** #1180 · #1068 · #1128 · #1071

**Details.** On Linux 2.7.5, `Ctrl+N` produces a file already containing
`programa { funcao inicio() { escreva("Olá Mundo") } }`. #1128 reports an error when creating or
opening a project. #1071 ("código embaralhado") is likely the same template being loaded into an
existing buffer.

**Root cause (*hypothesis*).** `TelaPrincipal.criarNovoCodigoFonte()` (`:276-290`) calls
`AbaCodigoFonte.novaAba()`, which loads a template resource; the 2.7.5 packaging apparently ships the
example template where the empty template should be.

**Possible fix.** Verify the packaged resource name (`modelo.por` vs `exemplo.por`) in
`ide/src/main/resources`, and make the template explicit per action ("Novo arquivo" vs "Novo a partir
do exemplo"). Optionally add a preference to start from an empty file.

**Acceptance criteria.** `Ctrl+N` yields an empty editor with the cursor on line 1.

---

### PS-054 · Editor text input is corrupted on some Linux keyboard layouts / IMEs
`P2` · `MED` · **Labels:** `bug`, `Linux Bugs` · **Status:** `needs-repro` · **Issues:** #1139 · #1141 · #975 · #1079

**Details.** #1139 — pressing `=` inserts `-` on a Debian layout. #1141 — typing **replaces the
selected text plus one extra character** (`Frase` → `Frse` when fixing the missing `r`). #975 —
specific accents (`à é ô ã`) do not type on Elementary OS. #1079 — on macOS, after switching to
another application the editor becomes read-only until restart.

**Root cause (*hypothesis*).** The editor performs its own key/modifier mapping (a Brazilian ABNT
assumption) in the key-binding layer instead of letting Swing's `InputMethod`/`Keymap` handle it;
#1141 additionally suggests the "overwrite selection" path is applied twice (once by the
`DocumentFilter`, once by the default editor kit).

**Possible fix.** Remove hard-coded key-code translation for printable characters, delegate to
`InputMethodListener`, and make the `DocumentFilter` idempotent (replace the selection exactly once).
For #1079, re-enable the component on window focus regain.

**Acceptance criteria.** Typing over a selection on Debian replaces only the selection; `=` inserts
`=` on an ABNT-2 and on a US layout.

---

### PS-055 · Missing basic editor features: auto-indent, multi-cursor, RTF copy, Nerd-font/plugin keys
`P3` · `MED` · **Labels:** `feature`, `enhancement` · **Status:** open · **Issues:** #1075 · #1107 · #1092 · #661 · #1096 · #1093 · #1197 · #820 · #451 · #416 · #559 · #948 · #1106 · #1033

**Details.** Quality-of-life requests that keep coming back for years:
* #1075 / #1107 — automatic indentation (asked as far back as #411, five years earlier);
* #1092 — the editor keeps re-centering the selected line, making long selections jump;
* #661 — multi-cursor editing (already available in Portugol WebStudio);
* #1096 — copy as **RTF** so pasted code keeps its colours (WebStudio already does this);
* #1093 — stop storing IDE metadata as comments in the `.por` file, use a sidecar JSON (this is why
  a `.por` diff changes when only the cursor moved);
* #1197 — (N)vim/Emacs key bindings, and put the project URL in `leia.txt`;
* #820 — GitHub integration and "open folder" side panel;
* #451 — export a `.exe`/`.jar` (3 of its 4 checkboxes are unchecked);
* #416 — a visual form designer (drag-and-drop GUI);
* #559 — `struct` support;
* #948 — a unit-test plugin for teachers;
* #1106 — an online judge that accepts Portugol.

**Possible fix.** Group them into three tracks: **(1) editor polish** (#1075/#1107/#1092, all
`RSyntaxTextArea` configuration — `setAutoIndentEnabled`, `setTabsEmulated`, centring toggle);
**(2) low-risk wins** (#1096 RTF copy is ~30 lines in `Editor`'s copy action; #1093 sidecar JSON needs a
format version and read-fallback); **(3) research** (#820/#451/#416/#559/#948/#1106 — each needs a
design issue, not a patch). `#451` and `#559` are already labelled `postponed`.

**Acceptance criteria.** Auto-indent on Enter; copy→paste into Word keeps colours; the IDE no longer
rewrites `.por` files when only the caret moved.

---

### PS-056 · Help viewer, themes and tab labels break
`P2` · `MIN` · **Labels:** `bug`, `Ajuda Error`, `Interface bugs` · **Status:** open · **Issues:** #939 · #1129 · #1163

**Details.** #939 — clicking an `https` link in the help raises
`TipoUrlInvalidoException: … foi passado o tipo "https que não é considerado válido"` (note the
truncated type string — the parser clips one character). #1163 — `OutOfMemoryError` in
`AbaAjuda.propertyChange(AbaAjuda.java:618)` (the CSS-box renderer leaks). #1129 — the theme cannot be
changed at all.

**Possible fix.** In `SwingBrowserHyperlinkHandler.decodificarTipo`, accept any scheme
(`https`, `http`, `file`) instead of whitelisting two and slicing the string; open external links in
`Desktop.browse` rather than in the embedded renderer. Cache/evict help pages (or reuse one
`JEditorPane`) to avoid the OOM. For #1129 verify the theme files are present in the 2.7.5 package.

**Acceptance criteria.** Help links open; the help tab can be opened/closed indefinitely without an
OOM; the theme menu works.

---

### PS-057 · "It doesn't work" reports that need a reproducer
`P1` · `MED` · **Labels:** none (mostly unlabelled) · **Status:** `needs-repro` · **Issues:** #1059 · #1131 · #1140 · #1142 · #1167 · #1196

**Details.** These describe a real symptom but ship no runnable `.por` file, so no code path can be
confirmed:
* #1131 — after `leia(var)`, the variable does not appear in the inspector, and the program then
  claims `leia` is missing; the screenshot shows the drag-to-inspector panel empty → **likely PS-051**.
* #1142 — `leia` of a declared-but-uninitialised variable fails; the user believes the language
  refuses to read a variable before assignment.
* #1140 — `flag`-controlled loops do not stop and the IDE sometimes hangs (screenshots only).
* #1167 — a saved file cannot be reopened → **likely PS-003**.
* #1059 — a screenshot of an unexplained error dialog (`bug`, no text).
* #1196 — `escreva` prints `undefined` when concatenating an `inteiro` in **Portugol WebStudio**
  (`portugol.dev`). This is a *different* code base from this repository; it is listed for
  completeness and should be redirected to the WebStudio project.

**Possible fix.** Ask for the `.por` file plus the "copiar erro" output using the improved reporter
(PS-060), then either close as a duplicate of the parent item or promote to its own item. #1140
(loop that never terminates) is the only one that plausibly hides a *new* defect — give it a
reproduction attempt with the code from the screenshots.

**Acceptance criteria.** Each issue is either closed as duplicate or has a minimal reproducer and
its own item.

---

### PS-058 · Anti-aliasing/selection contrast in the dark theme
`P3` · `MIN` · **Labels:** `bug`, `Interface bugs` · **Status:** open · **Issue:** #931

**Details.** In dark mode the selection highlight is too light against the text and cannot be changed
through the theme files.

**Possible fix.** Add `selection.background`/`selection.foreground` keys to the theme JSON and map
them into `RSyntaxTextArea`'s `Theme`; ship a darker default for the dark theme.

**Acceptance criteria.** The dark theme's selection is readable and user-overridable.

---

### F · Project infrastructure, tooling & documentation

### PS-060 · The bug reporter files issues with no information, then abandons them
`P1` · `MED` · **Labels:** `bug`, `no-response`, `couldn't reproduce` · **Status:** open

**Issues (the whole `Erro numero …` family):** all **96** auto-generated reports. **28 of them contain
only the template text** and no stack trace, so they cannot be triaged at all:
#982 · #997 · #1013 · #1021 · #1029 · #1031 · #1036 · #1061 · #1101 · #1104 · #1109 · #1110 ·
#1111 · #1116 · #1117 · #1118 · #1123 · #1136 · #1152 · #1156 · #1157 · #1166 · #1174 · #1193 ·
#1207 · #1211 (and #1068 is a human-filed empty issue).

**Details.** Clicking "Relatar um Bug" opens GitHub with the template
`[pressione o botao "copiar erro" no Portugol Studio e cole o erro aqui antes de enviar]` — and
nothing else. Roughly 40 % of the open backlog is this noise, which is exactly why 149 of 243 issues
carry no label and 13 are marked `couldn't reproduce`.

**Root cause (*verified*).** The template lives in the packaging resources; the "copiar erro" button
copies to the **clipboard** and the issue body is never populated from the log. Also, the reporter
cannot tell duplicates apart, so the same crash is filed 9 times (appendix B, family 1).

**Possible fix.**
1. Prefill the issue body with the stack trace, app version, OS, Java version and locale (strip user
   paths / usernames).
2. Search for an existing issue with the same crash hash before creating a new one; if found, add a
   👍/comment ("também aconteceu comigo") instead of opening a duplicate.
3. Refuse to open an issue whose body still contains the template placeholder.
4. Add a `needs-info` bot label and auto-close after 30 days — the `no-response` label already exists
   but nothing acts on it.

**Acceptance criteria.** No new issue can be created without a stack trace or a description; repeat
crashes of the same hash do not create new issues.

---

### PS-061 · There is no automated test suite protecting the compiler
`P1` · `MAX` · **Labels:** `feature`, `discussion` · **Status:** open · `has-PR` (#985)

**Issues:** *(this item has no open issue of its own — see PS-055 #948, the teacher asking for a testing plugin, and PR #985)*

**Details.** #948 is a teacher asking whether unit-testing can be taught with Portugol. More
importantly for this backlog: nearly every item in section C would be caught by a snapshot test
("source `.por` → expected output" or "source → expected diagnostics"). The repo has a `checker`
module and `core` tests, but there is no corpus of `.por` fixtures wired to the analyzer.
PR #985 proposes `maxParallelForks` to speed the build up — a prerequisite for making that suite
practical.

**Possible fix.**
1. Add `core/src/test/resources/programas/<issue-number>.por` plus an expected-output file, and a
   JUnit parameterised test that compiles+executes each one through `Portugol`/`Compilador`.
2. Seed the corpus with the reproduction snippets already quoted in this document (#632, #893,
   #899, #915, #924, #1048, #1049, #1050, #1153, #1198 …) — they are free regression tests.
3. Merge #985 to keep the suite fast.
4. Add a CI workflow (GitHub Actions) that runs `./gradlew test` on Linux, macOS and Windows.

**Acceptance criteria.** Every bug fixed in sections C/D ships with a fixture; CI is green on all
three operating systems.

---

### PS-062 · Documentation: wrong `escolha` example, stale download links, update check
`P2` · `MIN` · **Labels:** `Ajuda Error`, `optimization` · **Status:** open · **Issues:** #892 · #916 · #1197

**Details.** #1120 — the help example uses `cadeia` with `escolha` (see PS-020). #892 — the internal
update link points at `lite.acad.univali.br/portugol`, which goes down under load; switch to
`portugolstudio.com.br` or the GitHub Pages site. #916 — the error messages are written for
programmers, not for beginners (`"A expressão não foi iniciada corretamente"` means nothing to a
first-time student). #1197 — `leia.txt` inside the zip does not point at the project.

**Possible fix.** Correct the `escolha` lesson (or fix the feature); move the update URL into a
single configuration constant and add a fallback list; rewrite the top 10 most frequent messages in
beginner language ("Você esqueceu o `(` depois de `escreva`") and keep the technical text in a
collapsed "detalhes técnicos" section; add the URL to `leia.txt`.

**Acceptance criteria.** No help page contradicts the compiler; the update check works when the
primary host is down.

---

### PS-063 · Distribution & repository hygiene
`P3` · `MIN` · **Labels:** various · **Status:** open · **Issues:** #532· #825· #901· #1077· #1025· #1197·

**Details.** A handful of small repository/CI tasks not worth their own item: `.gitattributes` for
line endings (PS-014), restoring the `standalone` artefact (PS-012), the release-checklist entry
"verify the `.dmg` mounts" (PS-010), and replacing this tracker's 149 unlabelled issues with a
triage sweep (labels already exist: `bug`, `feature`, `enhancement`, `couldn't reproduce`,
`no-response`, `postponed`).

**Possible fix.** One PR: extend `.gitattributes`, add a `RELEASE_CHECKLIST.md` with the per-OS smoke
test, and run a labelling pass over the unlabelled issues (a triage script using the labels above).

**Acceptance criteria.** Every open issue has at least one area label; the release checklist is
versioned.

---

### PS-064 · Silent failures and misleading messages elsewhere
`P2` · `MIN` · **Labels:** `Bad Error message` · **Status:** open · **Issues:** #698 · #914

**Details.** #698 — clicking "report bug" does nothing and no error is logged (the reporter itself is
broken for some users, related to PS-060). #914 — NPE in `Lancador.maximize` reported as
"Não foi possível iniciar o Portugol Studio" with no hint (see PS-001).

**Possible fix.** Make "report bug" log its own failure to `~/.portugol/log` and fall back to opening
the issues page in the browser; add the exception class name to the fatal dialog so reports are
actionable.

**Acceptance criteria.** A failing "report bug" produces a log line and a fallback URL.

---

### G · Not actionable as filed

### PS-070 · Non-issues, questions and test submissions
`P3` · `MIN` · **Labels:** *(none — triage only)* · **Status:** open · **Mode:** close as `invalid`/`question`

**Issues:** #1133 (body: `.`) · #1147 (body: `sla`) · #1068 (empty) · #1176 (body: `ola`) ·
#1143 (body: `jailton`) · #1098 (how to sort a `cadeia` vector alphabetically — a question)

**Possible fix.** Close the empty ones (`#1133`, `#1147`, `#1068`, `#1176`), move the "how do I…"
questions to GitHub **Discussions** (the repo already uses Discussions — #1168 was migrated from
one), and convert the open-ended ideas into a single "roadmap / ideas" discussion so the issue
tracker only holds actionable bugs.

**Acceptance criteria.** The issue tracker contains no empty issues and no pure questions.

---

## Appendix A · Item → issues map (generated)

Every open issue is owned by exactly **one** item (verified programmatically: 243 numbers assigned,
0 duplicates, 0 unassigned). Issues that other items mention as *related symptoms* are listed here
only under their owner, so this table and appendix C are the single source of truth.

| Item | Pri | Dif | Area | Owned issues | n |
|------|:---:|:---:|:----:|--------------|:-:|
| **PS-001** App does not start — `NullPointerException` while m… | P0 | MIN | A | #914, #1015, #1063, #1072, #1154, #1185, #1186, #1200, #1214, #1215, #1216, #1219 | 12 |
| **PS-002** Saving a file throws `NullPointerException` / `File… | P0 | MIN | A | #808, #979, #992, #1008, #1009, #1014, #1018, #1023, #1027, #1041, #1053, #1058, #1076, #1082, #1089, #1113, #1122, #1124, #1125, #1127, #1130, #1134, #1135, #1138, #1169, #1188, #1189, #1191, #1194, #1195, #1212 | 31 |
| **PS-003** Save appears to succeed but nothing is written (sil… | P0 | MED | A | #793, #961, #969, #989, #1002, #1028, #1171, #1203 | 8 |
| **PS-004** Recovered-temporary-files dialog floods the IDE on… | P1 | MED | A | #1114, #1201 | 2 |
| **PS-005** `portugol-console -no-wait` exits with code 0 on ru… | P0 | MIN | A | #1218 | 1 |
| **PS-010** macOS: `.dmg` reported as corrupted, Catalina+ hide… | P1 | MAX | B | #239, #812, #876, #880, #940, #990, #1081, #1126, #1187, #1204 | 10 |
| **PS-011** Upgrade from Java 8 to a supported JDK and from Web… | P1 | MAX | B | #421, #816, #840, #858, #862, #891, #984, #1064, #1069, #1094, #1206 | 11 |
| **PS-012** Linux distribution: no Flatpak, no standalone packa… | P2 | MED | B | #302, #901, #978 | 3 |
| **PS-013** Installer problems: silent install, custom target d… | P2 | MED | B | #768, #825, #925, #1025, #1077, #1150, #1168 | 7 |
| **PS-014** CRLF line endings break the Linux post-install scri… | P3 | MIN | B | #532 | 1 |
| **PS-015** Linux: hangs, freeze on start-up and "must reboot t… | P2 | MED | B | #1022, #1074, #1178, #1209 | 4 |
| **PS-020** `escolha` rejects `cadeia`, but the built-in help d… | P1 | MED | C | #1120, #1137, #1153 | 3 |
| **PS-021** Vector/matrix size must be a literal or a constant… | P1 | MAX | C | #911, #1121, #1161, #1182, #1190, #1198 | 6 |
| **PS-022** A function cannot declare a vector/matrix return ty… | P2 | MAX | C | #1162 | 1 |
| **PS-023** `sorteia(min, max)` inside a vector initialiser thr… | P1 | MIN | C | #606, #632, #1037 | 3 |
| **PS-024** Escape sequences do not work in `caracter` literals… | P1 | MIN | C | #1049 | 1 |
| **PS-025** Concatenating a `logico` writes `true`/`false` inst… | P2 | MIN | C | #1050 | 1 |
| **PS-026** `'A' + 'B'` is type-checked as `cadeia` but evaluat… | P2 | MED | C | #1048 | 1 |
| **PS-027** `ClassCastException` / `NullPointerException` when… | P1 | MED | C | #929, #1085, #1145, #1205 | 4 |
| **PS-028** Parser: `^=` inside `para`, and `}` inside strings/… | P1 | MED | C | #893, #924, #1160 | 3 |
| **PS-029** `vetor[i]++` followed by `escreva` produces a seman… | P2 | MED | C | #680, #899 | 2 |
| **PS-030** `inicio()` accepts a return type; starting the grap… | P2 | MIN | C | #918, #920, #922 | 3 |
| **PS-031** Integer overflow is silent, and `-2147483648` is re… | P2 | MIN | C | #921, #1090, #1172 | 3 |
| **PS-032** Repeated compilation fails: `ClassFormatError`, "Er… | P2 | MED | C | #1017, #1052, #1091 | 3 |
| **PS-033** Two `NullPointerException`s in the compiler pipeline | P2 | MIN | C | #1007, #1202 | 2 |
| **PS-040** 3-digit hexadecimal colours are expanded to the wro… | P2 | MIN | D | #915 | 1 |
| **PS-041** `manter_visivel = falso` still forces the window on… | P2 | MIN | D | #917 | 1 |
| **PS-042** Object library corrupts addresses when objects are… | P1 | MIN | D | #994 | 1 |
| **PS-043** `escreva` breaks on non-UTF-8 consoles and mixes `\… | P1 | MED | D | #951, #1004, #1065 | 3 |
| **PS-044** Library-level issues: `sorteia` duplication, `Util`… | P3 | MIN | D | #601, #716, #841, #842, #843, #848, #913, #923, #1030, #1102, #1112 | 11 |
| **PS-045** Index/selection arithmetic crashes: `IndexOutOfBoun… | P2 | MED | D | #896, #957, #959, #970, #972, #988, #991, #1032, #1043, #1045, #1066, #1073, #1119, #1149, #1151, #1155, #1159, #1170, #1192, #1210 | 20 |
| **PS-050** Console tab stays on "Mensagens" and cannot be swit… | P0 | MED | E | #599, #1060, #1100, #1158, #1177, #1179 | 6 |
| **PS-051** Variable inspector shows stale/blank values for arr… | P1 | MED | E | #279, #887, #900, #1051, #1054, #1056 | 6 |
| **PS-052** Dialog/notification rendering: ghosting, duplicated… | P2 | MED | E | #1105, #1184 | 2 |
| **PS-053** New documents open with the "Olá Mundo" example ins… | P1 | MIN | E | #1068, #1071, #1128, #1180 | 4 |
| **PS-054** Editor text input is corrupted on some Linux keyboa… | P2 | MED | E | #975, #1079, #1139, #1141 | 4 |
| **PS-055** Missing basic editor features: auto-indent, multi-c… | P3 | MED | E | #416, #451, #559, #661, #820, #948, #1033, #1075, #1092, #1093, #1096, #1106, #1107, #1197 | 14 |
| **PS-056** Help viewer, themes and tab labels break | P2 | MIN | E | #939, #1129, #1163 | 3 |
| **PS-057** "It doesn't work" reports that need a reproducer | P1 | MED | E | #1059, #1131, #1140, #1142, #1167, #1196 | 6 |
| **PS-058** Anti-aliasing/selection contrast in the dark theme | P3 | MIN | E | #931 | 1 |
| **PS-060** The bug reporter files issues with no information,… | P1 | MED | F | #982, #997, #1013, #1021, #1029, #1031, #1036, #1061, #1101, #1104, #1109, #1110, #1111, #1116, #1117, #1118, #1123, #1136, #1152, #1156, #1157, #1166, #1174, #1193, #1207, #1211 | 26 |
| **PS-061** There is no automated test suite protecting the com… | P1 | MAX | F | *no open issue — driven by a PR or another item* | 0 |
| **PS-062** Documentation: wrong `escolha` example, stale downl… | P2 | MIN | F | #892, #916 | 2 |
| **PS-063** Distribution & repository hygiene | P3 | MIN | F | *no open issue — driven by a PR or another item* | 0 |
| **PS-064** Silent failures and misleading messages elsewhere | P2 | MIN | F | #698 | 1 |
| **PS-070** Non-issues, questions and test submissions | P3 | MIN | G | #1098, #1133, #1143, #1147, #1176 | 5 |

**Total:** 243 issue references → 243 distinct issues across 46 work items.

| Area | Items | Issues | Share |
|------|:-----:|-------:|------:|
| A · Data loss & startup blockers | 5 | 54 | 22.2 % |
| B · Platform & distribution | 6 | 36 | 14.8 % |
| C · Core language | 14 | 36 | 14.8 % |
| D · Runtime libraries | 6 | 37 | 15.2 % |
| E · Editor, inspector, console & UI | 9 | 46 | 18.9 % |
| F · Infrastructure, tooling & docs | 5 | 29 | 11.9 % |
| G · Not actionable as filed | 1 | 5 | 2.1 % |

**243 / 243 issues covered (100 %).**

## Appendix B · Auto-generated crash reports, deduplicated

96 issues carry an `Erro numero <sha1>` title — they were filed by the IDE's own reporter. Grouped by the first application stack frame they collapse into **20 trace families** plus one traceless bucket.

| # | Family (first application frame) | Reports | Issues | Owner |
|--:|----------------------------------|:-------:|--------|-------|
| B01 | `br.univali.ps.ui.abas.AbaCodigoFonte.salvaArquivo(AbaCodigoFonte.java:928)` | 25 | #992, #1008, #1014, #1023, #1027, #1041, #1053, #1058, #1076, #1082, #1089, #1122, #1124, #1125, #1127, #1130, #1134, #1135, #1138, #1188, #1189, #1191, #1194, #1195, #1212 | **PS-002** |
| B02 | `br.univali.ps.nucleo.PortugolStudio.iniciarNovaInstancia(PortugolStudio.java:268)` | 10 | #1063, #1072, #1154, #1185, #1186, #1200, #1214, #1215, #1216, #1219 | **PS-001** |
| B03 | `br.univali.ps.nucleo.TratadorExcecoes.uncaughtException(TratadorExcecoes.java:145)` | 6 | #957, #991, #1043, #1045, #1119, #1151 | **PS-045** |
| B04 | `br.univali.ps.ui.rstautil.PortugolParser.adicionarErroAviso(PortugolParser.java:257)` | 5 | #972, #988, #1149, #1159, #1192 | **PS-045** |
| B05 | `br.univali.ps.ui.abas.AbaCodigoFonte.salvaArquivo(AbaCodigoFonte.java:932)` | 3 | #1009, #1018, #1169 | **PS-002** |
| B06 | `br.univali.ps.ui.editor.Editor.selecionarTexto(Editor.java:1165)` | 2 | #970, #1073 | **PS-045** |
| B07 | `br.univali.portugol.nucleo.compilador.Compilador.carregaProgramaCompilado(Compilador.java:309)` | 2 | #1017, #1091 | **PS-032** |
| B08 | `br.univali.ps.ui.editor.Editor.posicionarCursor(Editor.java:1530)` | 2 | #1032, #1066 | **PS-045** |
| B09 | `br.univali.portugol.nucleo.asa.NoMatriz.getTipoResultante(NoMatriz.java:72)` | 2 | #1085, #1145 | **PS-027** |
| B10 | `br.univali.ps.ui.editor.Editor.highlightLinha(Editor.java:1441)` | 1 | #959 | **PS-045** |
| B11 | `br.univali.portugol.nucleo.execucao.gerador.helpers.Utils.getNomeTipoJava(Utils.java:282)` | 1 | #1007 | **PS-033** |
| B12 | `br.univali.ps.ui.paineis.PainelExemplos.atualizarPainelDireita(PainelExemplos.java:634)` | 1 | #1033 | **PS-053** |
| B13 | `br.univali.ps.ui.inspetor.InspetorDeSimbolos.adicionaNoVetor(InspetorDeSimbolos.java:662)` | 1 | #1054 | **PS-051** |
| B14 | `br.univali.ps.ui.abas.AbaCodigoFonte.salvaArquivoRecuperavel(AbaCodigoFonte.java:965)` | 1 | #1114 | **PS-002** |
| B15 | `br.univali.ps.ui.utils.UnzipUtility.unzip(UnzipUtility.java:35)` | 1 | #1150 | **PS-013** |
| B16 | `br.univali.ps.nucleo.TratadorExcecoes.transformarExcecao(TratadorExcecoes.java:278)` | 1 | #1155 | **PS-045** |
| B17 | `br.univali.ps.ui.abas.abaAjuda.AbaAjuda.propertyChange(AbaAjuda.java:618)` | 1 | #1163 | **PS-056** |
| B18 | `br.univali.portugol.nucleo.BuscadorDeSimbolo.visitar(BuscadorDeSimbolo.java:603)` | 1 | #1202 | **PS-033** |
| B19 | `br.univali.ps.ui.utils.FileHandle.read(FileHandle.java:78)` | 1 | #1203 | **PS-045** |
| B20 | `br.univali.portugol.nucleo.execucao.gerador.PreCompilador.visitar(PreCompilador.java:128)` | 1 | #1205 | **PS-027** |
| B21 | *(no stack trace — template text only)* | 28 | #982, #997, #1013, #1015, #1021, #1029, #1031, #1036, #1061, #1101, #1104, #1109, #1110, #1111, #1113, #1116, #1117, #1118, #1123, #1136, #1152, #1156, #1157, #1166, #1174, #1193, #1207, #1211 | **PS-060** |

**Reading the table.** The two `salvaArquivo` families (B01 + B05) account for **28 reports** — the single most-reported defect in the project, and the reason PS-002 is P0. The 28 traceless reports in the last row contain no technical information at all, which is why PS-060 (fix the reporter) is a prerequisite for keeping this tracker usable.

## Appendix C · Authoritative index — all 243 open issues

Sorted by number; `Owner` is the item that must close it.

| # | Title | Owner | Pri | Dif |
|--:|-------|-------|:---:|:---:|
| 239 | Mac - clique em arquivos .por abre o PS mas não o arquivo clicado | PS-010 | P1 | MAX |
| 279 | Melhorias no inspetor de variáveis | PS-051 | P1 | MED |
| 302 | Flatpak build | PS-012 | P2 | MED |
| 416 | Windows Forms, JFrame | PS-055 | P3 | MED |
| 421 | Problema de renderização/duplicação do Portugol Studio (Soluções e Di… | PS-011 | P1 | MAX |
| 451 | Gerar um exe a partir de um programa em portugol feito por um usuário | PS-055 | P3 | MED |
| 532 | Erros nos arquivos de Linux | PS-014 | P3 | MIN |
| 559 | Suporte para structs | PS-055 | P3 | MED |
| 599 | Inspetor não deixa mudar as abas | PS-050 | P0 | MED |
| 601 | Função obter_diretorio_usuario() | PS-044 | P3 | MIN |
| 606 | Função sorteia(minimo, maximo) | PS-023 | P1 | MIN |
| 632 | Bug com a função sorteia(min, max) | PS-023 | P1 | MIN |
| 661 | Editar multiplas linhas/pontos ao mesmo tempo | PS-055 | P3 | MED |
| 680 | Expressões numéricas e operadores unitários | PS-029 | P2 | MED |
| 698 | Falha ao reportar bug | PS-064 | P2 | MIN |
| 716 | Sugestão de novas funções para a biblioteca Util e manipulação de vet… | PS-044 | P3 | MIN |
| 768 | Erro de instalação de plugin no macOS 700ec85c8c1a9adcdcf33ba306eb666… | PS-013 | P2 | MED |
| 793 | Tratar Erro Permissão negada Linux | PS-003 | P0 | MED |
| 808 | Erro ao Salvar arquivo | PS-002 | P0 | MIN |
| 812 | Portugol não abre na versão 10.15.5 do mac | PS-010 | P1 | MAX |
| 816 | Ao redimensionar a janela a aplicação trava (dois monitores) | PS-011 | P1 | MAX |
| 820 | Integração com o github - feature -discussion | PS-055 | P3 | MED |
| 825 | Unknown error while running (...)desinstalar.exe -- mode unattended | PS-013 | P2 | MED |
| 840 | Não pôde criar Maquina Virtual Java | PS-011 | P1 | MAX |
| 841 | função escreva com "\n" embutido | PS-044 | P3 | MIN |
| 842 | Função leia com timeout | PS-044 | P3 | MIN |
| 843 | estrutura de repetição tipo "repita" | PS-044 | P3 | MIN |
| 848 | alerta ao usar a função valor_absoluto da biblioteca matemática | PS-044 | P3 | MIN |
| 858 | Movimentação entre monitores | PS-011 | P1 | MAX |
| 862 | NoClassDefFound com.sun.net.ssl.internal.ssl.Provider | PS-011 | P1 | MAX |
| 876 | Portugol Studio não vê arquivos em MacOS Catalina 10.15.4+ | PS-010 | P1 | MAX |
| 880 | Erro crash do JAVA no macOS Big Sur | PS-010 | P1 | MAX |
| 887 | Inspetor não printa valores de Vetore/Matrizes muito grandes, quando… | PS-051 | P1 | MED |
| 891 | Atualizar JAVA e WebLAF | PS-011 | P1 | MAX |
| 892 | Trocar link de atualização interno do Portugol | PS-062 | P2 | MIN |
| 893 | problema ao utilizar op. XOR dentro de um loop | PS-028 | P1 | MED |
| 896 | Erro java.util.Vector.elementAt ArrayIndexOutOfBoundsException: 1 >=… | PS-045 | P2 | MED |
| 899 | Erro gerado quando um vetor é incrementado com "++" seguido de um esc… | PS-029 | P2 | MED |
| 900 | Valor no vetor não aparece | PS-051 | P1 | MED |
| 901 | Disponibilização do pacote standalone para Linux | PS-012 | P2 | MED |
| 911 | Inicializar vetor com variavel | PS-021 | P1 | MAX |
| 913 | Função transladar | PS-044 | P3 | MIN |
| 914 | Erro Lancador.maximize v2.7.5 | PS-001 | P0 | MIN |
| 915 | Cores hexadecimais de 3 dígitos erradas | PS-040 | P2 | MIN |
| 916 | Textos mais informativos para os iniciantes | PS-062 | P2 | MIN |
| 917 | A janela continua visível, mesmo com manter_visivel sendo falso | PS-041 | P2 | MIN |
| 918 | Iniciar duas vezes o modo gráfico gera erro null | PS-030 | P2 | MIN |
| 920 | Função inicio() aceita retornar valor | PS-030 | P2 | MIN |
| 921 | Portugol avisa que o intervalo de bits é um número, mas considera out… | PS-031 | P2 | MIN |
| 922 | Biblioteca PortugolStudio gera erro quando executada | PS-030 | P2 | MIN |
| 923 | Novos métodos de escrita no console | PS-044 | P3 | MIN |
| 924 | Fecha chaves dentro de texto gera erro | PS-028 | P1 | MED |
| 925 | Nullpointer ao tentar instalar um plugin com um arquivo errado/inexis… | PS-013 | P2 | MED |
| 929 | Erro com código utilizando referencias com vetor | PS-027 | P1 | MED |
| 931 | Trocar a cor da seleção no modo escuro. | PS-058 | P3 | MIN |
| 939 | Ajuda.SwingBrowserHyperlinkHandler.decodificarTipo 38e11c2227ed584013… | PS-056 | P2 | MIN |
| 940 | Encerramento | PS-010 | P1 | MAX |
| 948 | Execução de testes (unitários) | PS-055 | P3 | MED |
| 951 | Texto em vetor não aceita caracteres especiais | PS-043 | P1 | MED |
| 957 | noticeContainsPosition Erro numero 399e0c62585647868f3ac4661ca94e7397… | PS-045 | P2 | MED |
| 959 | Editor.highlightLinha Erro numero 23d99ecaeaf2d1429aaec3b859948aa9090… | PS-045 | P2 | MED |
| 961 | Não esta salvando  | PS-003 | P0 | MED |
| 969 | salvar | PS-003 | P0 | MED |
| 970 | Editor.selecionarTexto Erro numero 79a4b4f72913f13dbeca2a73d4ac467c72… | PS-045 | P2 | MED |
| 972 | rstautil.PortugolParser.adicionarErroAviso Erro numero 980ab9dd7a6a3c… | PS-045 | P2 | MED |
| 975 | Problemas em acentos específicos no Linux | PS-054 | P2 | MED |
| 978 | Adicionar releases do Tipo JAR ou versões para armhf / aarch64 (Raspb… | PS-012 | P2 | MED |
| 979 | Erro de salvamento no Ubunto 2716522b68d06a35a2cd349e1c8520f73bc7bbd4… | PS-002 | P0 | MIN |
| 982 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 984 | Uso Windows 11 com resolução 4k e as fontes do Portugolo ficaram extr… | PS-011 | P1 | MAX |
| 988 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 989 | não sei mais o que fazer | PS-003 | P0 | MED |
| 990 | Portugol não abre no Mac Monterey  | PS-010 | P1 | MAX |
| 991 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 992 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 994 | Biblioteca de objetos não administra corretamente os endereços dos ob… | PS-042 | P1 | MIN |
| 997 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1002 | Não salvou código | PS-003 | P0 | MED |
| 1004 | Problemas com o "escreva" | PS-043 | P1 | MED |
| 1007 | Erro numero … v2.7.5 | PS-033 | P2 | MIN |
| 1008 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1009 | Erro numero … v2.7.4 | PS-002 | P0 | MIN |
| 1013 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1014 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1015 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1017 | Erro numero … v2.7.5 | PS-032 | P2 | MED |
| 1018 | Erro numero … v2.7.4 | PS-002 | P0 | MIN |
| 1021 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1022 | Travamentos no Zorin  | PS-015 | P2 | MED |
| 1023 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1025 | Instalação Personalizada | PS-013 | P2 | MED |
| 1027 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1028 | Versão 2.7.5 não está salvando o código | PS-003 | P0 | MED |
| 1029 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1030 | Ao chamar funções da biblioteca com "." não abre funções | PS-044 | P3 | MIN |
| 1031 | Erro numero … v2.7.4 | PS-060 | P1 | MED |
| 1032 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1033 | Erro numero … v2.7.5 | PS-055 | P3 | MED |
| 1036 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1037 | Erro ao chamar funcao sorteia sem biblioteca dentro de um vetor | PS-023 | P1 | MIN |
| 1041 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1043 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1045 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1048 | Operação de soma com tipo caracter indica que retorna tipo cadeia por… | PS-026 | P2 | MED |
| 1049 | Caractere de escape \ não funciona no tipo Caracter | PS-024 | P1 | MIN |
| 1050 | Operação de concatenação entre cadeia e lógico escreve o seu valor em… | PS-025 | P2 | MIN |
| 1051 | Erro na exibição de variáveis no painel de variáveis | PS-051 | P1 | MED |
| 1052 | Portugol - Erro na compilação | PS-032 | P2 | MED |
| 1053 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1054 | Erro numero … v2.7.4 | PS-051 | P1 | MED |
| 1056 | Variáveis não são adicionadas no inspetor que continua limpo | PS-051 | P1 | MED |
| 1058 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1059 | bug | PS-057 | P1 | MED |
| 1060 | Problema ao abrir janela para um novo programa | PS-050 | P0 | MED |
| 1061 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1063 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1064 | Bug no Dell G15 Windows 11 | PS-011 | P1 | MAX |
| 1065 | Erro Texto | PS-043 | P1 | MED |
| 1066 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1068 | Portugol Studio | PS-053 | P1 | MIN |
| 1069 | Personalizar o tamanho da janela | PS-011 | P1 | MAX |
| 1071 | Código embaralhado | PS-053 | P1 | MIN |
| 1072 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1073 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1074 | portugol studio travando, fica processando e trava o pc. | PS-015 | P2 | MED |
| 1075 | Autoformatação (identação) | PS-055 | P3 | MED |
| 1076 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1077 | Instalação Silenciosa | PS-013 | P2 | MED |
| 1079 | não aceita edição | PS-054 | P2 | MED |
| 1081 | Não instala - MAC OS  | PS-010 | P1 | MAX |
| 1082 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1085 | Erro numero … v2.7.5 | PS-027 | P1 | MED |
| 1089 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1090 | Numeros quebrados não entrando no conceito certo | PS-031 | P2 | MIN |
| 1091 | Erro numero … v2.7.5 | PS-032 | P2 | MED |
| 1092 | Centraliza linha celecionada | PS-055 | P3 | MED |
| 1093 | Sugestão: JSON com informações de arquivo | PS-055 | P3 | MED |
| 1094 | Aumentar o tamanho da fonte no menu lateral à direita assim como no m… | PS-011 | P1 | MAX |
| 1096 | Copiar e Colar Código do Editor como RTF | PS-055 | P3 | MED |
| 1098 | Ordenar um vetor do tipo cadeia em ordem alfabética | PS-070 | P3 | MIN |
| 1100 | Um bug no portugol | PS-050 | P0 | MED |
| 1101 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1102 | Melhoria na exibição de números reais na linguagem Portugol através d… | PS-044 | P3 | MIN |
| 1104 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1105 | Ambiente gráfico em tela cheia piscando ao iniciar o programa | PS-052 | P2 | MED |
| 1106 | Online Judge  | PS-055 | P3 | MED |
| 1107 | identação no Portugol Studio | PS-055 | P3 | MED |
| 1109 | Erro numero … v2.7.4 | PS-060 | P1 | MED |
| 1110 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1111 | Erro numero … v2.7.4 | PS-060 | P1 | MED |
| 1112 | Como posso arredondar um numero decimal no portugol? | PS-044 | P3 | MIN |
| 1113 | Erro numero … v2.7.4 | PS-002 | P0 | MIN |
| 1114 | Erro numero … v2.7.4 | PS-004 | P1 | MED |
| 1116 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1117 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1118 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1119 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1120 | Erro na documentação do ESCOLHA-CASO | PS-020 | P1 | MED |
| 1121 | Portugol Webstudio não está aceitando tipo inteiro constante como tam… | PS-021 | P1 | MAX |
| 1122 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1123 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1124 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1125 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1126 | Erro ao abrir Portugal studio no Mac OS | PS-010 | P1 | MAX |
| 1127 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1128 | Criação de projeto | PS-053 | P1 | MIN |
| 1129 | Não consigo mudar o tema no portugol | PS-056 | P2 | MIN |
| 1130 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1131 | erro no comando leia | PS-057 | P1 | MED |
| 1133 | . | PS-070 | P3 | MIN |
| 1134 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1135 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1136 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1137 | Diferentes posturas entre portugol para android e portugol para deskt… | PS-020 | P1 | MED |
| 1138 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1139 | Erro de sinais | PS-054 | P2 | MED |
| 1140 | Bug no portugol | PS-057 | P1 | MED |
| 1141 | Erro na codagem | PS-054 | P2 | MED |
| 1142 | Portugol studio help | PS-057 | P1 | MED |
| 1143 | jailton | PS-070 | P3 | MIN |
| 1145 | Erro numero … v2.7.5 | PS-027 | P1 | MED |
| 1147 | sla | PS-070 | P3 | MIN |
| 1149 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1150 | Erro numero … v2.7.5 | PS-013 | P2 | MED |
| 1151 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1152 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1153 | cadeia não funciona numa condição de escolha no aplicativo local | PS-020 | P1 | MED |
| 1154 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1155 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1156 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1157 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1158 | Algoritmo Roda de boa após feito, porém se fechar o Portugol e abrir,… | PS-050 | P0 | MED |
| 1159 | Erro numero … v2.7.5 (Erro desconhecido achado no portugol) | PS-045 | P2 | MED |
| 1160 | Bug de sintaxe mesmo que ela esteja correta | PS-028 | P1 | MED |
| 1161 | Erro ao criar e manipular matriz no Portugol Web | PS-021 | P1 | MAX |
| 1162 | Impossibilidade para criar uma função que retorne um vetor ou matriz | PS-022 | P2 | MAX |
| 1163 | Erro numero … v2.7.5 | PS-056 | P2 | MIN |
| 1166 | Erro numero … v2.7.4 | PS-060 | P1 | MED |
| 1167 | nao abre os arquivos salvos | PS-057 | P1 | MED |
| 1168 | Não está sendo possível baixar a última versão do Portugol | PS-013 | P2 | MED |
| 1169 | Erro numero … v2.7.4 | PS-002 | P0 | MIN |
| 1170 | erro desconhecido no portugol | PS-045 | P2 | MED |
| 1171 | Problemas com PORTUGOL STUDIO, será apenas um bug? | PS-003 | P0 | MED |
| 1172 | Potenciação de inteiros com expoente grande | PS-031 | P2 | MIN |
| 1174 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1176 | ola | PS-070 | P3 | MIN |
| 1177 | Tela travada na aba de mensagem | PS-050 | P0 | MED |
| 1178 | Nao abre o app quando eu fecho no linux mint | PS-015 | P2 | MED |
| 1179 | Portugol não dá o resultado do programa | PS-050 | P0 | MED |
| 1180 | Olá mundo | PS-053 | P1 | MIN |
| 1182 | Erro de sintaxe e outros ao executar o algoritmo da matriz identidade | PS-021 | P1 | MAX |
| 1184 | BUG no portugol | PS-052 | P2 | MED |
| 1185 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1186 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1187 | Arquivo .dmg do portugol corrompido no mac | PS-010 | P1 | MAX |
| 1188 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1189 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1190 | Relatar um Bug | PS-021 | P1 | MAX |
| 1191 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1192 | Erro numero … v2.7.5 | PS-045 | P2 | MED |
| 1193 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1194 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1195 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1196 | Bug de Impressão: escreva não exibe variáveis inteiro e concatenação… | PS-057 | P1 | MED |
| 1197 | Plugin para (N)vim | PS-055 | P3 | MED |
| 1198 | Definição de índice de vetor ocorrendo erro. | PS-021 | P1 | MAX |
| 1200 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1201 | ERRO DE RECUPERAÇÃO DE ARQUIVOS TEMPORÁRIOS | PS-004 | P1 | MED |
| 1202 | Erro numero … v2.7.4 | PS-033 | P2 | MIN |
| 1203 | Erro numero … v2.7.5 | PS-003 | P0 | MED |
| 1204 | Portugol-Studio-2.7.5 essa versao nao instala no macOs 12 | PS-010 | P1 | MAX |
| 1205 | Erro numero … v2.7.5 | PS-027 | P1 | MED |
| 1206 | Bug Portugol | PS-011 | P1 | MAX |
| 1207 | Erro numero … v2.7.4 | PS-060 | P1 | MED |
| 1209 | Falha no terminal da tela cheia do Portogol Studio | PS-015 | P2 | MED |
| 1210 | erro ao salvar arquivo | PS-045 | P2 | MED |
| 1211 | Erro numero … v2.7.5 | PS-060 | P1 | MED |
| 1212 | Erro numero … v2.7.5 | PS-002 | P0 | MIN |
| 1214 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1215 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1216 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |
| 1218 | portugol-console: com -no-wait, erro de execução encerra com código 0… | PS-005 | P0 | MIN |
| 1219 | Erro numero … v2.7.5 | PS-001 | P0 | MIN |

**Count: 243 rows.**

## Appendix D · Open pull requests

| PR | Title | What it does | Unblocks |
|----|-------|--------------|----------|
| [#1220](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/1220) | `feat: suporte a tamanho dinâmico de vetores e matrizes` | runtime-sized arrays in local scope, validated at run time | **PS-021** |
| [#1097](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/1097) | `Aumentar a compatibilidade do Portugol-Studio com o macOS` | workaround for the corrupted `.dmg` | **PS-010** |
| [#985](https://github.com/UNIVALI-LITE/Portugol-Studio/pull/985) | `Improve GRADLE build performance` | `maxParallelForks` for the test suite | **PS-061** |

## Appendix E · Referenced but closed, or out of scope

| Reference | Seen in | Note |
|-----------|---------|------|
| #411 | #1075 | closed; the original auto-indent request, five years older |
| #80 · #231 · #371 · #417 | #421 | closed; earlier duplicates of the rendering bug |
| #995 | #994 | the open fix PR for the object-address bug |
| #1164 | #1168 | a Discussion migrated into an issue |
| #1062 | — | already fixed at the base commit of this checkout ("exemplo com carácter na ajuda") |
| #1196 | PS-057 | belongs to **Portugol WebStudio** (`portugol.dev`), a separate code base |

---

*Generated 2026-11-17 from the GitHub API snapshot described in §0. Every count in these appendices is
recomputed from that snapshot rather than transcribed by hand; appendix A is the machine-checked proof
that all 243 open issues are accounted for.*

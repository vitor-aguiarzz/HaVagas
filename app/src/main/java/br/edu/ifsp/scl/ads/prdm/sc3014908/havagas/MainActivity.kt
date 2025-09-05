package br.edu.ifsp.scl.ads.prdm.sc3014908.havagas

import android.R
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3014908.havagas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val formationTypes = arrayOf(
        "Fundamental", "Médio", "Graduação", "Especialização", "Mestrado", "Doutorado"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFormationSpinnerDataListeners()
        setButtonsListeners()
    }

    fun setFormationSpinnerDataListeners() {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, formationTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFormacao.adapter = adapter

        binding.spinnerFormacao.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                showFormationFields(formationTypes[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                binding.layoutCamposFormacao.removeAllViews()
            }
        }
    }

    fun setButtonsListeners() {
        binding.checkAdicionarCelular.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutCelular.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.buttonSalvar.setOnClickListener {
            val dados = getFilledData()
            showResum(dados)
        }

        binding.buttonLimpar.setOnClickListener {
            cleanForm()
        }
    }

    private fun makeLabel(ctx: Context, text: String): TextView {
        val label = TextView(ctx)
        label.text = text
        return label
    }

    private fun makeTextField(ctx: Context, hint: String, inputType: Int, tag: String): EditText {
        val field = EditText(ctx)
        field.id = View.generateViewId()
        field.hint = hint
        field.inputType = inputType
        field.tag = tag
        return field
    }

    private fun showSchoolFields(ctx: Context) {
        binding.layoutCamposFormacao.addView(makeLabel(ctx, "Ano de formatura"))
        val anoEdit = makeTextField(ctx, "Ano", InputType.TYPE_CLASS_NUMBER, "anoFormatura")
        binding.layoutCamposFormacao.addView(anoEdit)
    }

    private fun showPHDFields(ctx: Context) {
        addConclusionYearInstitutionFields(ctx)

        binding.layoutCamposFormacao.addView(makeLabel(ctx, "Título da Monografia"))
        val tituloEdit = makeTextField(ctx, "Título", InputType.TYPE_CLASS_TEXT, "tituloMonografia")
        binding.layoutCamposFormacao.addView(tituloEdit)

        binding.layoutCamposFormacao.addView(makeLabel(ctx, "Orientador"))
        val orientEdit = makeTextField(ctx, "Orientador", InputType.TYPE_CLASS_TEXT, "orientador")
        binding.layoutCamposFormacao.addView(orientEdit)
    }

    private fun addConclusionYearInstitutionFields(ctx: Context) {
        binding.layoutCamposFormacao.addView(makeLabel(ctx, "Ano de conclusão"))
        val anoEdit = makeTextField(ctx, "Ano", InputType.TYPE_CLASS_NUMBER, "anoConclusao")
        binding.layoutCamposFormacao.addView(anoEdit)

        binding.layoutCamposFormacao.addView(makeLabel(ctx, "Instituição"))
        val instEdit = makeTextField(ctx, "Instituição", InputType.TYPE_CLASS_TEXT, "instituicao")
        binding.layoutCamposFormacao.addView(instEdit)
    }

    private fun showFormationFields(type: String) {
        binding.layoutCamposFormacao.removeAllViews()
        val ctx = binding.root.context

        when (type) {
            "Fundamental", "Médio" -> {
                showSchoolFields(ctx)
            }

            "Graduação", "Especialização" -> {
                addConclusionYearInstitutionFields(ctx)
            }

            "Mestrado", "Doutorado" -> {
                showPHDFields(ctx)
            }
        }
    }

    private fun getFilledData(): String {
        val sb = StringBuilder()
        sb.append("Nome: ${binding.editNome.text}\n")
        sb.append("E-mail: ${binding.editEmail.text}\n")
        sb.append("Recebe atualizações: ${if (binding.checkAtualizacoes.isChecked) "Sim" else "Não"}\n")
        sb.append("Telefone: ${binding.editTelefone.text}\n")

        val tipoTel = when (binding.radioTipoTelefone.checkedRadioButtonId) {
            binding.radioComercial.id -> "Comercial"
            binding.radioResidencial.id -> "Residencial"
            else -> "Não informado"
        }

        sb.append("Tipo de telefone: $tipoTel\n")
        if (binding.checkAdicionarCelular.isChecked) {
            sb.append("Celular: ${binding.editCelular.text}\n")
        }
        val sexo = when (binding.radioSexo.checkedRadioButtonId) {
            binding.radioMasculino.id -> "Masculino"
            binding.radioFeminino.id -> "Feminino"
            else -> "Não informado"
        }
        sb.append("Sexo: $sexo\n")
        sb.append("Data de nascimento: ${binding.editNascimento.text}\n")
        val formacao = binding.spinnerFormacao.selectedItem.toString()
        sb.append("Formação: $formacao\n")

        for (i in 0 until binding.layoutCamposFormacao.childCount) {
            val v = binding.layoutCamposFormacao.getChildAt(i)
            if (v is EditText) {
                val tag = v.tag?.toString() ?: ""
                sb.append("$tag: ${v.text}\n")
            }
        }
        sb.append("Vagas de interesse: ${binding.editVagasInteresse.text}\n")
        return sb.toString()
    }

    private fun showResum(dados: String) {
        AlertDialog.Builder(this)
            .setTitle("Resumo do Cadastro")
            .setMessage(dados)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun cleanForm() {
        binding.editNome.text?.clear()
        binding.editEmail.text?.clear()
        binding.checkAtualizacoes.isChecked = false
        binding.editTelefone.text?.clear()
        binding.radioTipoTelefone.clearCheck()
        binding.checkAdicionarCelular.isChecked = false
        binding.editCelular.text?.clear()
        binding.radioSexo.clearCheck()
        binding.editNascimento.text?.clear()
        binding.spinnerFormacao.setSelection(0)
        binding.layoutCamposFormacao.removeAllViews()
        binding.editVagasInteresse.text?.clear()
    }
}